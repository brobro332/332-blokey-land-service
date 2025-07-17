package xyz.samsami.blokey_land.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectWithTaskResponseDto;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.type.TaskStatusType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @InjectMocks private ProjectService service;
    @Mock private BlokeyService blokeyService;
    @Mock private MemberService memberService;
    @Mock private ProjectRepository repository;

    private UUID blokeyId;
    private Blokey blokey;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = Blokey.builder()
            .id(blokeyId)
            .nickname("닉네임")
            .bio("소개")
            .build();
    }

    @DisplayName("유효한 파라미터가 주어졌을 때 프로젝트와 멤버를 생성해야 한다.")
    @Test
    void givenValidParameter_whenCreateProject_thenProjectAndMemberShouldBeCreated() {
        // given
        Project project = mock(Project.class);
        ProjectReqCreateDto dto = ProjectReqCreateDto.builder()
            .title("제목")
            .description("명세")
            .imageUrl("이미지 URL")
            .isPrivate(false)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();

        when(repository.save(any(Project.class))).thenReturn(project);
        when(blokeyService.findBlokeyByBlokeyId(blokeyId)).thenReturn(blokey);

        // when
        service.createProject(dto, blokeyId.toString());

        // then
        verify(repository).save(any(Project.class));
        verify(memberService).createMember(project, blokey, RoleType.LEADER);
    }
    
    @DisplayName("사용자 ID가 주어졌을 때 프로젝트 응답 DTO 목록을 반환해야 한다.")
    @Test
    void givenValidBlokeyId_whenReadAllProjects_thenReturnProjectOnlyDtoList() {
        // given
        List<ProjectOnlyRespDto> dtoList = List.of(
            ProjectOnlyRespDto.builder()
                .id(1L)
                .title("테스트 프로젝트 1")
                .description("단위 테스트용 프로젝트 설명 1")
                .imageUrl("/image1")
                .status(ProjectStatusType.ACTIVE)
                .isPrivate(false)
                .isLeader(true)
                .estimatedStartDate(LocalDate.of(2025, 7, 1))
                .estimatedEndDate(LocalDate.of(2025, 12, 31))
                .actualStartDate(LocalDate.of(2025, 7, 3))
                .actualEndDate(null)
                .build(),
            ProjectOnlyRespDto.builder()
                .id(2L)
                .title("테스트 프로젝트 2")
                .description("단위 테스트용 프로젝트 설명 2")
                .imageUrl("/image1")
                .status(ProjectStatusType.ACTIVE)
                .isPrivate(false)
                .isLeader(true)
                .estimatedStartDate(LocalDate.of(2025, 7, 1))
                .estimatedEndDate(LocalDate.of(2025, 12, 31))
                .actualStartDate(LocalDate.of(2025, 7, 3))
                .actualEndDate(null)
                .build()
        );

        when(repository.findProjectsWithRoleByBlokeyId(blokeyId)).thenReturn(dtoList);
    
        // when
        List<ProjectOnlyRespDto> result = service.readAllProjects(blokeyId.toString());
    
        // then
        assertEquals(2, result.size());
    }

    @DisplayName("사용자 ID가 주어졌을 때 프로젝트 및 태스크 응답 DTO 목록을 반환해야 한다.")
    @Test
    void givenValidBlokeyId_whenReadAllProjectsWithTasks_thenReturnProjectWithTasksDtoList() {
        // given
        Project project = Project.builder()
            .id(1L)
            .title("테스트 프로젝트")
            .description("테스트 프로젝트 설명입니다.")
            .imageUrl("image")
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(false)
            .estimatedStartDate(LocalDate.of(2025, 7, 1))
            .estimatedEndDate(LocalDate.of(2025, 12, 31))
            .actualStartDate(LocalDate.of(2025, 7, 3))
            .actualEndDate(null)
            .build();

        Task task1 = Task.builder()
            .title("테스크 1")
            .description("테스트용 테스크 1입니다.")
            .status(TaskStatusType.TODO)
            .project(project)
            .build();

        Task task2 = Task.builder()
            .title("테스크 2")
            .description("테스트용 테스크 2입니다.")
            .status(TaskStatusType.IN_PROGRESS)
            .project(project)
            .build();

        Task task3 = Task.builder()
            .title("테스크 3")
            .description("테스트용 테스크 3입니다.")
            .status(TaskStatusType.DONE)
            .project(project)
            .build();

        project.addTask(task1);
        project.addTask(task2);
        project.addTask(task3);

        List<Project> projectList = List.of(project);

        when(repository.findProjectsWithTasksByBlokeyId(blokeyId)).thenReturn(projectList);

        // when
        List<ProjectWithTaskResponseDto> result = service.readAllProjectsWithTasks(blokeyId.toString());

        // then
        assertEquals(1, result.size());
        assertEquals(3, result.getFirst().getTasks().size());
    }
    
    @DisplayName("존재하는 ID로 프로젝트를 조회하면 해당 객체를 반환해야 한다.")
    @Test
    void givenValidId_whenReadProjectByProjectId_thenReturnProject() {
        // given
        Long id = 1L;
        Project project = Project.builder()
            .id(id)
            .title("제목")
            .description("설명")
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();

        when(repository.findById(id)).thenReturn(Optional.of(project));

        // when
        Project found = service.findProjectByProjectId(id);
    
        // then
        assertEquals(project.getId(), found.getId());
        assertEquals(project.getTitle(), found.getTitle());
        assertEquals(project.getDescription(), found.getDescription());
        assertEquals(project.getImageUrl(), found.getImageUrl());
        assertEquals(project.getStatus(), found.getStatus());
        assertEquals(project.isPrivate(), found.isPrivate());
        assertEquals(project.getEstimatedStartDate(), found.getEstimatedStartDate());
        assertEquals(project.getEstimatedEndDate(), found.getEstimatedEndDate());
        assertEquals(project.getActualStartDate(), found.getActualStartDate());
        assertEquals(project.getActualEndDate(), found.getActualEndDate());

        verify(repository).findById(id);
    }

    @DisplayName("유효한 파라미터가 주어지면 프로젝트 정보가 수정되어야 한다.")
    @Test
    void givenValidParameter_whenUpdateProjectByProjectId_thenProjectShouldBeUpdated() {
        // given
        Long projectId = 1L;
        Project project = Project.builder()
            .id(projectId)
            .title("제목")
            .description("설명")
            .imageUrl("이미지 URL")
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();

        ProjectReqUpdateDto dto = ProjectReqUpdateDto.builder()
            .title("수정 제목")
            .description("수정 설명")
            .imageUrl("수정 이미지 URL")
            .status(ProjectStatusType.COMPLETED)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();

        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        service.updateProjectByProjectId(projectId, dto);

        // then
        assertEquals("수정 제목", project.getTitle());
        assertEquals(ProjectStatusType.COMPLETED, project.getStatus());
        assertTrue(project.isPrivate());

        verify(repository).findById(projectId);
    }

    @DisplayName("유효한 ID가 주어지면 프로젝트 상태를 DELETED로 변경해야 한다.")
    @Test
    void givenValidId_whenSoftDeleteProject_thenStatusShouldBeDeleted() {
        // given
        Long projectId = 1L;
        Project project = Project.builder()
            .id(projectId)
            .status(ProjectStatusType.ACTIVE)
            .build();

        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        service.softDeleteProjectByProjectId(projectId);

        // then
        assertEquals(ProjectStatusType.DELETED, project.getStatus());

        verify(repository).findById(projectId);
    }
}