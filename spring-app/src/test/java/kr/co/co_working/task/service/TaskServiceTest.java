package kr.co.co_working.task.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.repository.ProjectRepository;
import kr.co.co_working.project.Project;
import kr.co.co_working.project.service.ProjectService;
import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;
import kr.co.co_working.task.repository.TaskRepository;
import kr.co.co_working.task.Task;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTest {
    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectService projectService;

    @Autowired
    TeamService teamService;

    @Autowired
    MemberService memberService;

    @Autowired
    ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        String email = "test@korea.kr";
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void createTask() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        TaskRequestDto.CREATE taskDto = getCreateTaskDto(projectId, email);

        /* when */
        Long taskId = taskService.createTask(taskDto);

        /* then */
        Project project = projectRepository.findById(projectId).get();
        Task task = taskRepository.findById(taskId).get();

        Assertions.assertEquals(projectDto.getName(), project.getName());
        Assertions.assertEquals(projectDto.getDescription(), project.getDescription());
        Assertions.assertEquals(0, project.getTasks().size() - 1);

        Assertions.assertEquals(taskDto.getName(), task.getName());
        Assertions.assertEquals(taskDto.getType(), task.getType());
        Assertions.assertEquals(taskDto.getDescription(), task.getDescription());
        Assertions.assertEquals(taskDto.getProjectId(), task.getProject().getId());
        Assertions.assertEquals(20, task.getName().length());
        Assertions.assertEquals(taskDto.getStartAt(), task.getStartAt());
        Assertions.assertEquals(taskDto.getEndAt(), task.getEndAt());
    }

    @Test
    public void readTaskList() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        TaskRequestDto.CREATE taskDto = getCreateTaskDto(projectId, email);
        taskService.createTask(taskDto);

        /* when */
        List<TaskResponseDto> tasks = taskService.readTaskList(TaskRequestDto.READ.builder()
            .name("")
            .type("")
            .description("")
            .startAt(LocalDateTime.of(2024, 9, 11, 0, 0, 0))
            .endAt(LocalDateTime.of(2024, 9, 5, 0, 0, 0))
            .build());

        /* then */
        Assertions.assertEquals(1, tasks.size());
    }

    @Test
    public void updateTask() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        List<Long> taskIdList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            TaskRequestDto.CREATE taskDto = new TaskRequestDto.CREATE();
            taskDto.setName("테스트 " + i);
            taskDto.setType("텍스트 " + i);
            taskDto.setDescription("금주 프로젝트 개발 건에 대한 테스트 " + i);
            taskDto.setProjectId(projectId);
            taskDto.setStartAt(LocalDateTime.of(2024, 9, 5, 0, 0, 0));
            taskDto.setEndAt(LocalDateTime.of(2024, 9, 11, 0, 0, 0));

            taskIdList.add(taskService.createTask(taskDto));

            /* when */
            taskService.updateTask(taskIdList.get(0),
                    new TaskRequestDto.UPDATE(
                            projectId,
                            "수정된 테스트",
                            "라디오 버튼",
                            "수정된 명세",
                            LocalDateTime.of(2024, 9, 5, 0, 0, 0),
                            LocalDateTime.of(2024, 9, 11, 0, 0, 0)
                    )
            );
            taskDto = null;
        }
        Project project = projectRepository.findById(projectId).get();
        Task task = taskRepository.findById(taskIdList.get(0)).get();

        /* then */
        Assertions.assertEquals(2, project.getTasks().size());

        Assertions.assertEquals("수정된 테스트", task.getName());
        Assertions.assertEquals(task.getName(), project.getTasks().get(0).getName());

        Assertions.assertEquals("라디오 버튼", task.getType());
        Assertions.assertEquals(task.getType(), project.getTasks().get(0).getType());

        Assertions.assertEquals("수정된 명세", task.getDescription());
        Assertions.assertEquals(task.getDescription(), project.getTasks().get(0).getDescription());

        Assertions.assertEquals(task.getStartAt(), LocalDateTime.of(2024, 9, 5, 0, 0, 0));
        Assertions.assertEquals(task.getEndAt(), LocalDateTime.of(2024, 9, 11, 0, 0, 0));
    }

    @Test
    public void deleteTask() throws Exception {
        /* given */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        MemberRequestDto.CREATE memberDto = getCreateMemberDto();
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = getCreateTeamDto(memberDto);
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = getCreateProjectDto(teamId);
        Long projectId = projectService.createProject(projectDto);

        List<Long> taskIdList = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            TaskRequestDto.CREATE taskDto = new TaskRequestDto.CREATE();
            taskDto.setName("테스트 " + i);
            taskDto.setType("텍스트 " + i);
            taskDto.setDescription("금주 프로젝트 개발 건에 대한 테스트 " + i);
            taskDto.setProjectId(projectId);
            taskDto.setStartAt(LocalDateTime.of(2024, 9, 5, 0, 0, 0));
            taskDto.setEndAt(LocalDateTime.of(2024, 9, 11, 0, 0, 0));

            taskIdList.add(taskService.createTask(taskDto));
            taskDto = null;
        }

        TaskRequestDto.DELETE taskDeleteDto = new TaskRequestDto.DELETE();
        taskDeleteDto.setProjectId(projectId);

        /* when */
        taskService.deleteTask(taskIdList.get(0), taskDeleteDto);

        /* then */
        Project project = projectRepository.findById(projectId).get();
        Task task = taskRepository.findById(taskIdList.get(1)).get();

        Assertions.assertEquals("테스트 2", task.getName());
        Assertions.assertEquals("텍스트 2", task.getType());
        Assertions.assertEquals("금주 프로젝트 개발 건에 대한 테스트 2", task.getDescription());

        Assertions.assertEquals(1, project.getTasks().size());
    }

    /**
     * 
     * @return getCreateMemberDto : Member DTO 생성
     * @throws Exception
     */
    private MemberRequestDto.CREATE getCreateMemberDto() throws Exception {
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        return memberDto;
    }

    /**
     * getCreateTeamDto : Team DTO 생성
     * @param memberDto
     * @return
     */
    private static TeamRequestDto.CREATE getCreateTeamDto(MemberRequestDto.CREATE memberDto) {
        TeamRequestDto.CREATE teamDto = new TeamRequestDto.CREATE();
        teamDto.setName("팀명 1");
        teamDto.setDescription("팀 소개입니다.");
        return teamDto;
    }

    /**
     * getCreateTaskDto : Task DTO 생성
     * @param projectId
     * @param email
     * @return
     */
    private static TaskRequestDto.CREATE getCreateTaskDto(Long projectId, String email) {
        TaskRequestDto.CREATE taskDto = new TaskRequestDto.CREATE();
        taskDto.setName("테스트테스트테스트테스트테스트테스트테스");
        taskDto.setType("텍스트");
        taskDto.setDescription("금주 프로젝트 개발 건에 대한 테스트");
        taskDto.setProjectId(projectId);
        taskDto.setStartAt(LocalDateTime.of(2024, 9, 5, 0, 0, 0));
        taskDto.setEndAt(LocalDateTime.of(2024, 9, 11, 0, 0, 0));
        return taskDto;
    }

    /**
     * getCreateProjectDto : Project DTO 생성
     * @param teamId
     * @return
     */
    private static ProjectRequestDto.CREATE getCreateProjectDto(Long teamId) {
        ProjectRequestDto.CREATE projectDto = new ProjectRequestDto.CREATE();
        projectDto.setName("프로젝트 A");
        projectDto.setDescription("프로젝트 관리 프로그램 만들기");
        projectDto.setTeamId(teamId);
        return projectDto;
    }
}