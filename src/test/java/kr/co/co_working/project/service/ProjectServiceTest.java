package kr.co.co_working.project.service;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.service.MemberService;
import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.dto.ProjectResponseDto;
import kr.co.co_working.project.repository.ProjectRepository;
import kr.co.co_working.project.Project;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProjectServiceTest {
    @Autowired
    ProjectService service;

    @Autowired
    TeamService teamService;

    @Autowired
    MemberService memberService;

    @Autowired
    ProjectRepository repository;

    @Test
    public void createProject() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = new TeamRequestDto.CREATE();
        teamDto.setName("팀명 1");
        teamDto.setDescription("팀 소개입니다.");
        teamDto.setEmail(memberDto.getEmail());
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = new ProjectRequestDto.CREATE();
        projectDto.setName("프로젝트 A");
        projectDto.setDescription("프로젝트 관리 프로그램 만들기");
        projectDto.setTeamId(teamId);

        /* when */
        Long projectId = service.createProject(projectDto);

        /* then */
        Project project = repository.findById(projectId).get();
        Assertions.assertEquals("프로젝트 A", project.getName());
        Assertions.assertEquals("프로젝트 관리 프로그램 만들기", project.getDescription());
        Assertions.assertEquals(0, project.getTasks().size());
    }

    @Test
    public void readProjectList() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = new TeamRequestDto.CREATE();
        teamDto.setName("팀명 1");
        teamDto.setDescription("팀 소개입니다.");
        teamDto.setEmail(memberDto.getEmail());
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = new ProjectRequestDto.CREATE();
        projectDto.setName("프로젝트 A");
        projectDto.setDescription("프로젝트 관리 프로그램 만들기");
        projectDto.setTeamId(teamId);
        service.createProject(projectDto);

        /* when */
        List<ProjectResponseDto> projects = service.readProjectList(new ProjectRequestDto.READ("젝트", teamId));

        /* then */
        Assertions.assertEquals(1, projects.size());
    }

    @Test
    public void updateProject() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = new TeamRequestDto.CREATE();
        teamDto.setName("팀명 1");
        teamDto.setDescription("팀 소개입니다.");
        teamDto.setEmail(memberDto.getEmail());
        Long teamId = teamService.createTeam(teamDto);

        ProjectRequestDto.CREATE projectDto = new ProjectRequestDto.CREATE();
        projectDto.setName("프로젝트 A");
        projectDto.setDescription("프로젝트 관리 프로그램 만들기");
        projectDto.setTeamId(teamId);
        Long projectId = service.createProject(projectDto);
        Project project = repository.findById(projectId).get();

        /* when */
        service.updateProject(projectId, new ProjectRequestDto.UPDATE("프로젝트 B", "명세 수정", teamId));

        /* then */
        Assertions.assertEquals("프로젝트 B", project.getName());
        Assertions.assertEquals("명세 수정", project.getDescription());
        Assertions.assertEquals(0, project.getTasks().size());
    }

    @Test
    public void deleteProject() throws Exception {
        /* given */
        MemberRequestDto.CREATE memberDto = new MemberRequestDto.CREATE();
        memberDto.setEmail("test@korea.kr");
        memberDto.setPassword("1234");
        memberDto.setName("김아무개");
        memberDto.setDescription("test");
        memberService.createMember(memberDto);

        TeamRequestDto.CREATE teamDto = new TeamRequestDto.CREATE();
        teamDto.setName("팀명 1");
        teamDto.setDescription("팀 소개입니다.");
        teamDto.setEmail(memberDto.getEmail());
        Long teamId = teamService.createTeam(teamDto);

        List<Long> idList = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            ProjectRequestDto.CREATE dto = new ProjectRequestDto.CREATE();
            dto.setName("프로젝트 " + i);
            dto.setDescription("프로젝트 관리 프로그램 만들기 " + i);
            dto.setTeamId(teamId);
            idList.add(service.createProject(dto));
            dto = null;
        }

        /* when */
        repository.delete(repository.findById(idList.get(0)).get());

        /* then */
        Project project = repository.findById(idList.get(1)).get();
        Assertions.assertEquals("프로젝트 2", project.getName());
        Assertions.assertEquals("프로젝트 관리 프로그램 만들기 2", project.getDescription());
        Assertions.assertEquals(1, repository.findAll().size());
    }
}