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
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @InjectMocks
    ProjectService service;

    @Mock
    MemberService memberService;

    @Mock
    BlokeyService blokeyService;

    @Mock
    ProjectRepository repository;

    @Test
    void createProject_validInput_success() {
        // given
        UUID blokeyId = UUID.randomUUID();

        ProjectReqCreateDto dto = ProjectReqCreateDto.builder()
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(blokeyId)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        Blokey blokey = Blokey.builder()
                .id(blokeyId)
                .nickname("짱구")
                .bio("감자머리입니다.")
                .build();

        when(blokeyService.findBlokeyByBlokeyId(blokeyId)).thenReturn(blokey);

        Project project = Project.builder()
            .id(1L)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(blokeyId)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();
        when(repository.save(any(Project.class))).thenReturn(project);

        // when
        service.createProject(dto);

        // then
        ArgumentCaptor<Project> projectCaptor = ArgumentCaptor.forClass(Project.class);
        verify(repository, times(1)).save(projectCaptor.capture());
        Project capturedProject = projectCaptor.getValue();

        assertEquals(dto.getTitle(), capturedProject.getTitle());
        assertEquals(dto.getDescription(), capturedProject.getDescription());
        assertEquals(dto.getOwnerId(), capturedProject.getOwnerId());
        assertEquals(dto.getEstimatedStartDate(), capturedProject.getEstimatedStartDate());
        assertEquals(dto.getEstimatedEndDate(), capturedProject.getEstimatedEndDate());
        assertEquals(dto.getActualStartDate(), capturedProject.getActualStartDate());
        assertEquals(dto.getActualEndDate(), capturedProject.getActualEndDate());

        verify(memberService, times(1)).createMember(project, blokey);
    }

    @Test
    void readProjects_validInput_true() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        UUID blokeyId1 = UUID.randomUUID();
        UUID blokeyId2 = UUID.randomUUID();

        Project project1 = Project.builder()
            .id(1L)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(blokeyId1)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();
        Project project2 = Project.builder()
            .id(2L)
            .title("CINE-FINDER PROJECT")
            .description("사용자 입력에 따른 영화 상영정보 제공 서비스입니다.")
            .ownerId(blokeyId2)
            .estimatedStartDate(LocalDate.now().plusDays(2))
            .estimatedEndDate(LocalDate.now().plusDays(3))
            .actualStartDate(LocalDate.now().plusDays(4))
            .actualEndDate(LocalDate.now().plusDays(5))
            .build();

        List<Project> projects = List.of(project1, project2);
        Page<Project> page = new PageImpl<>(projects, pageable, projects.size());

        when(repository.findAll(pageable)).thenReturn(page);

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
            assertEquals(project.getEstimatedStartDate(), dto.getEstimatedStartDate());
            assertEquals(project.getEstimatedEndDate(), dto.getEstimatedEndDate());
            assertEquals(project.getActualStartDate(), dto.getActualStartDate());
            assertEquals(project.getActualEndDate(), dto.getActualEndDate());
        }

        verify(repository, times(1)).findAll(pageable);
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
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        ProjectRespDto result = service.readProjectByProjectId(projectId);

        assertEquals(project.getTitle(), result.getTitle());
        assertEquals(project.getDescription(), result.getDescription());
        assertEquals(project.getOwnerId(), result.getOwnerId());
        assertEquals(project.getEstimatedStartDate(), result.getEstimatedStartDate());
        assertEquals(project.getEstimatedEndDate(), result.getEstimatedEndDate());
        assertEquals(project.getActualStartDate(), result.getActualStartDate());
        assertEquals(project.getActualEndDate(), result.getActualEndDate());

        verify(repository, times(1)).findById(projectId);
    }

    @Test
    void updateProjectByProjectId_validInput_true() {
        // given
        Long projectId = 1L;
        UUID blokeyId1 = UUID.randomUUID();

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(blokeyId1)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        UUID blokeyId2 = UUID.randomUUID();
        ProjectReqUpdateDto dto = ProjectReqUpdateDto.builder()
            .title("CINE-FINDER PROJECT")
            .description("사용자 입력에 따른 영화 상영정보 제공 서비스입니다.")
            .ownerId(blokeyId2)
            .estimatedStartDate(LocalDate.now().plusDays(2))
            .estimatedEndDate(LocalDate.now().plusDays(3))
            .actualStartDate(LocalDate.now().plusDays(4))
            .actualEndDate(LocalDate.now().plusDays(5))
            .build();

        // when
        service.updateProjectByProjectId(projectId, dto);
        ProjectRespDto result = service.readProjectByProjectId(projectId);

        // then
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getOwnerId(), result.getOwnerId());
        assertEquals(dto.getEstimatedStartDate(), result.getEstimatedStartDate());
        assertEquals(dto.getEstimatedEndDate(), result.getEstimatedEndDate());
        assertEquals(dto.getActualStartDate(), result.getActualStartDate());
        assertEquals(dto.getActualEndDate(), result.getActualEndDate());
    }

    @Test
    void softDeleteProjectByProjectId_validInput_true() {
        // given
        Long projectId = 1L;
        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        service.softDeleteProjectByProjectId(projectId);

        // then
        assertTrue(project.isDeleted());
    }

    @Test
    void softDeleteProjectByProjectId_invalidInput_false() {
        // given
        Long projectId = 999L;

        when(repository.findById(projectId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () ->
            service.softDeleteProjectByProjectId(projectId)
        );

        assertEquals(ExceptionType.NOT_FOUND, e.getException());
    }

    @Test
    void restoreProjectByProjectId_validInput_true() {
        // given
        Long projectId = 1L;
        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        service.restoreProjectByProjectId(projectId);

        // then
        assertFalse(project.isDeleted());
    }
}