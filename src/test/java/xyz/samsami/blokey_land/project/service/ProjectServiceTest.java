package xyz.samsami.blokey_land.project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @InjectMocks
    ProjectService service;

    @Mock
    UserService userService;

    @Mock
    ProjectRepository repository;

    @Mock
    Project project;

    @Test
    void createProject_validInput_true() {
        // given
        UUID userId = UUID.randomUUID();

        ProjectReqCreateDto dto = ProjectReqCreateDto.builder()
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(userId)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(2))
            .build();

        User user = User.builder()
            .id(userId)
            .nickname("짱구")
            .bio("감자머리입니다.")
            .build();

        when(userService.findUserByUserId(userId)).thenReturn(user);

        // when
        service.createProject(dto);

        // then
        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(repository, times(1)).save(captor.capture());
        Project capturedProject = captor.getValue();

        assertEquals(dto.getTitle(), capturedProject.getTitle());
        assertEquals(dto.getDescription(), capturedProject.getDescription());
        assertEquals(dto.getOwnerId(), capturedProject.getOwnerId());
        assertEquals(dto.getStartDate(), capturedProject.getStartDate());
        assertEquals(dto.getEndDate(), capturedProject.getEndDate());
    }

    @Test
    void readProjects_validInput_true() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();

        Project project1 = Project.builder()
            .id(1L)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(userId1)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(2))
            .build();
        Project project2 = Project.builder()
            .id(2L)
            .title("CINE-FINDER PROJECT")
            .description("사용자 입력에 따른 영화 상영정보 제공 서비스입니다.")
            .ownerId(userId2)
            .startDate(LocalDate.now().plusDays(2))
            .endDate(LocalDate.now().plusDays(5))
            .build();

        List<Project> projects = List.of(project1, project2);
        Page<Project> page = new PageImpl<>(projects, pageable, projects.size());

        when(repository.findPageAll(pageable)).thenReturn(page);

        // when
        Page<ProjectRespDto> result = service.readProjects(pageable);

        // then
        assertEquals(projects.size(), result.getNumberOfElements());

        for (int i = 0; i < projects.size(); i++) {
            ProjectRespDto dto = result.getContent().get(i);
            Project project = projects.get(i);

            assertEquals(project.getTitle(), dto.getTitle());
            assertEquals(project.getDescription(), dto.getDescription());
            assertEquals(project.getOwnerId(), dto.getOwnerId());
            assertEquals(project.getStartDate(), dto.getStartDate());
            assertEquals(project.getEndDate(), dto.getEndDate());
        }

        verify(repository, times(1)).findPageAll(pageable);
    }

    @Test
    void readProjectByProjectId_validInput_true() {
        // given
        Long projectId = 1L;

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(2))
            .build();

        when(repository.findOptionalById(projectId)).thenReturn(Optional.of(project));

        // when
        ProjectRespDto result = service.readProjectByProjectId(projectId);

        assertEquals(project.getTitle(), result.getTitle());
        assertEquals(project.getDescription(), result.getDescription());
        assertEquals(project.getOwnerId(), result.getOwnerId());
        assertEquals(project.getStartDate(), result.getStartDate());
        assertEquals(project.getEndDate(), result.getEndDate());

        verify(repository, times(1)).findOptionalById(projectId);
    }

    @Test
    void updateProjectByProjectId_input_true() {
        // given
        Long projectId = 1L;
        UUID userId1 = UUID.randomUUID();

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(userId1)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(2))
            .build();

        when(repository.findOptionalById(projectId)).thenReturn(Optional.of(project));

        UUID userId2 = UUID.randomUUID();
        ProjectReqUpdateDto dto = ProjectReqUpdateDto.builder()
            .title("CINE-FINDER PROJECT")
            .description("사용자 입력에 따른 영화 상영정보 제공 서비스입니다.")
            .ownerId(userId2)
            .startDate(LocalDate.of(2024, 2, 1))
            .endDate(LocalDate.of(2024, 11, 30))
            .build();

        // when
        service.updateProjectByProjectId(projectId, dto);
        ProjectRespDto result = service.readProjectByProjectId(projectId);

        // then
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getOwnerId(), result.getOwnerId());
        assertEquals(dto.getStartDate(), result.getStartDate());
        assertEquals(dto.getEndDate(), result.getEndDate());
    }

    @Test
    void deleteProjectByProjectId_validInput_true() {
        // given
        Long projectId = 1L;
        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(2))
            .build();

        when(repository.findOptionalById(projectId)).thenReturn(Optional.of(project));

        // when
        service.deleteProjectByProjectId(projectId);

        // then
        verify(repository, times(1)).delete(project);
    }

    @Test
    void deleteProjectByProjectId_invalidInput_false() {
        // given
        Long projectId = 999L;

        when(repository.findOptionalById(projectId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () ->
            service.deleteProjectByProjectId(projectId)
        );

        assertEquals(ExceptionType.NOT_FOUND, e.getException());
    }
}