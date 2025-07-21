package xyz.samsami.blokey_land.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.domain.ProjectDiscipline;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;
import xyz.samsami.blokey_land.discipline.service.DisciplineService;
import xyz.samsami.blokey_land.discipline.service.ProjectDisciplineService;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectWithTaskRespDto;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.project.service.helper.ProjectAttachHelper;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;
import xyz.samsami.blokey_land.skill.service.ProjectSkillService;
import xyz.samsami.blokey_land.skill.service.SkillService;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.type.TaskStatusType;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @InjectMocks private ProjectService service;
    @Mock BlokeyService blokeyService;
    @Mock MemberService memberService;
    @Mock ProjectRepository repository;
    @Mock SkillService skillService;
    @Mock DisciplineService disciplineService;
    @Mock ProjectSkillService projectSkillService;
    @Mock ProjectDisciplineService projectDisciplineService;
    @Mock ProjectAttachHelper projectAttachHelper;
    @Mock ApplicationEventPublisher publisher;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Long projectId;
    Project project;
    String title = "테스트_제목_텍스트";
    String description = "테스트_설명_텍스트";
    String imageUrl = "테스트_이미지_URL_텍스트";
    LocalDate estimatedStartDate = LocalDate.now();
    LocalDate estimatedEndDate = LocalDate.now();
    LocalDate actualStartDate = LocalDate.now();
    LocalDate actualEndDate = LocalDate.now();

    Skill skill1;
    Skill skill2;
    ProjectSkill projectSkill1;
    ProjectSkill projectSkill2;

    Discipline discipline1;
    Discipline discipline2;
    ProjectDiscipline projectDiscipline1;
    ProjectDiscipline projectDiscipline2;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = Blokey.builder()
            .id(blokeyId)
            .nickname(nickname)
            .bio(bio)
            .build();

        projectId = 1L;
        project = Project.builder()
            .title(title)
            .description(description)
            .imageUrl(imageUrl)
            .isPrivate(true)
            .estimatedStartDate(estimatedStartDate)
            .estimatedEndDate(estimatedEndDate)
            .actualStartDate(actualStartDate)
            .actualEndDate(actualEndDate)
            .build();

        skill1 = Skill.builder().id(1L).name("java").displayName("Java").build();
        skill2 = Skill.builder().id(2L).name("python").displayName("Python").build();

        projectSkill1 = ProjectSkill.builder().project(project).skill(skill1).build();
        projectSkill2 = ProjectSkill.builder().project(project).skill(skill2).build();
        project.addSkill(projectSkill1);
        project.addSkill(projectSkill2);

        discipline1 = Discipline.builder().id(1L).name("프론트엔드 개발").build();
        discipline2 = Discipline.builder().id(2L).name("백엔드 개발").build();
        projectDiscipline1 = ProjectDiscipline.builder().project(project).discipline(discipline1).build();
        projectDiscipline2 = ProjectDiscipline.builder().project(project).discipline(discipline2).build();
        project.addDiscipline(projectDiscipline1);
        project.addDiscipline(projectDiscipline2);
    }

    @DisplayName("유효한 파라미터가 주어진다면_프로젝트를 생성할 때_모든 필드 값이 저장돼야 한다.")
    @Test
    void givenValidParameter_whenCreateProject_thenAllFieldsShouldBeSaved() {
        // given
        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        ProjectReqCreateDto dto = ProjectReqCreateDto.builder()
            .title(title)
            .description(description)
            .imageUrl(imageUrl)
            .isPrivate(true)
            .skills(List.of(1L, 2L))
            .disciplines(List.of(1L, 2L))
            .estimatedStartDate(estimatedStartDate)
            .estimatedEndDate(estimatedEndDate)
            .actualStartDate(actualStartDate)
            .actualEndDate(actualEndDate)
            .build();

        when(repository.save(any(Project.class))).thenReturn(project);
        when(blokeyService.findBlokeyByBlokeyId(blokeyId)).thenReturn(blokey);

        // when
        service.createProject(dto, blokeyId.toString());

        // then
        verify(repository).save(captor.capture());
        Project saved = captor.getValue();

        assertEquals(title, saved.getTitle());
        assertEquals(description, saved.getDescription());
        assertEquals(imageUrl, saved.getImageUrl());
        assertEquals(estimatedStartDate, saved.getEstimatedStartDate());
        assertEquals(estimatedEndDate, saved.getEstimatedEndDate());
        assertEquals(actualStartDate, saved.getActualStartDate());
        assertEquals(actualEndDate, saved.getActualEndDate());
        verify(projectSkillService, times(2)).create(any(), any());
        verify(projectDisciplineService, times(2)).create(any(), any());
    }

    @DisplayName("사용자 ID가 주어진다면_모든 프로젝트를 조회할 때_응답 객체 목록을 반환해야 한다.")
    @Test
    void givenBlokeyId_whenReadAllProjects_thenReturnsProjectOnlyDtoList() {
        // given
        List<ProjectOnlyRespDto> dtoList = List.of(
            ProjectOnlyRespDto.builder()
                .id(1L)
                .title(title + "_1")
                .description(description + "_1")
                .imageUrl(imageUrl + "_1")
                .status(ProjectStatusType.ACTIVE)
                .isPrivate(true)
                .isLeader(true)
                .estimatedStartDate(estimatedStartDate)
                .estimatedEndDate(estimatedEndDate)
                .actualStartDate(actualStartDate)
                .actualEndDate(actualEndDate)
                .build(),
            ProjectOnlyRespDto.builder()
                .id(2L)
                .title(title + "_2")
                .description(description + "_2")
                .imageUrl(imageUrl + "_2")
                .status(ProjectStatusType.ACTIVE)
                .isPrivate(true)
                .isLeader(true)
                .estimatedStartDate(estimatedStartDate)
                .estimatedEndDate(estimatedEndDate)
                .actualStartDate(actualStartDate)
                .actualEndDate(actualEndDate)
                .build()
        );

        when(repository.findProjectsByBlokeyId(blokeyId)).thenReturn(dtoList);
        when(projectAttachHelper.<ProjectOnlyRespDto>attachAll(anyList()))
            .thenAnswer(invocation -> {
                List<ProjectOnlyRespDto> inputList = invocation.getArgument(0);

                return inputList.stream()
                    .map(dto -> {
                        if (dto.getId() == 1L) {
                            return dto.toBuilder()
                                .skills(Set.of(new SkillRespDto(1L, "Java")))
                                .disciplines(Set.of(new DisciplineRespDto(1L, "프론트엔드 개발")))
                                .build();
                        } else if (dto.getId() == 2L) {
                            return dto.toBuilder()
                                .skills(Set.of(new SkillRespDto(2L, "Spring")))
                                .disciplines(Set.of(new DisciplineRespDto(2L, "백엔드 개발")))
                                .build();
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            });

        // when
        List<ProjectOnlyRespDto> result = service.readAllProjects(blokeyId.toString());

        // then
        assertEquals(2, result.size());
        assertThat(result.getFirst().getTitle()).contains("제목");
        assertThat(result.getFirst().getDescription()).contains("설명");
        assertThat(result.getFirst().getImageUrl()).contains("이미지");
        assertThat(result.getFirst().getIsLeader()).isTrue();
        assertTrue(result.stream().anyMatch(p -> p.getSkills().stream().anyMatch(s -> s.getName().equals("Java"))));
        assertTrue(result.stream().anyMatch(p -> p.getSkills().stream().anyMatch(s -> s.getName().equals("Spring"))));
        assertTrue(result.stream().anyMatch(p -> p.getDisciplines().stream().anyMatch(s -> s.getName().equals("프론트엔드 개발"))));
        assertTrue(result.stream().anyMatch(p -> p.getDisciplines().stream().anyMatch(s -> s.getName().equals("백엔드 개발"))));
    }

    @DisplayName("사용자 ID가 주어진다면_프로젝트와 태스크 목록을 조회할 때_프로젝트와 태스크 응답 객체 목록을 반환해야 한다.")
    @Test
    void givenBlokeyId_whenReadAllProjectsWithTasks_thenReturnsProjectWithTasksDtoList() {
        // given
        Task task1 = Task.builder()
            .title(title + "_1")
            .description(description + "_1")
            .status(TaskStatusType.TODO)
            .project(project)
            .build();

        Task task2 = Task.builder()
            .title(title + "_2")
            .description(description + "_2")
            .status(TaskStatusType.IN_PROGRESS)
            .project(project)
            .build();

        Task task3 = Task.builder()
            .title(title + "_3")
            .description(description + "_3")
            .status(TaskStatusType.DONE)
            .project(project)
            .build();

        project.addTask(task1);
        project.addTask(task2);
        project.addTask(task3);

        List<Project> projectList = List.of(project);

        when(repository.findProjectsWithTasksByBlokeyId(blokeyId)).thenReturn(projectList);
        when(projectAttachHelper.attachAll(ArgumentMatchers.<List<ProjectWithTaskRespDto>>any()))
            .thenAnswer(invocation -> {
                List<ProjectWithTaskRespDto> inputList = invocation.getArgument(0);

                return inputList.stream()
                    .map(dto -> dto.toBuilder()
                        .skills(Set.of(
                            new SkillRespDto(1L, "Java"),
                            new SkillRespDto(2L, "Python")
                        ))
                        .disciplines(Set.of(
                            new DisciplineRespDto(1L, "프론트엔드 개발"),
                            new DisciplineRespDto(2L, "백엔드 개발")
                        ))
                        .build()
                    )
                    .toList();
            });


        // when
        List<ProjectWithTaskRespDto> result = service.readAllProjectsWithTasks(blokeyId.toString());

        // then
        assertEquals(1, result.size());
        assertThat(result.getFirst().getTitle()).contains("제목");
        assertThat(result.getFirst().getDescription()).contains("설명");
        assertThat(result.getFirst().getImageUrl()).contains("이미지");
        assertThat(result.getFirst().getSkills().size()).isEqualTo(2);
        assertThat(result.getFirst().getDisciplines().size()).isEqualTo(2);
        assertEquals(3, result.getFirst().getTasks().size());
    }
    
    @DisplayName("존재하는 ID가 주어진다면_단일 프로젝트를 조회할 때_해당 프로젝트 엔티티를 반환해야 한다.")
    @Test
    void givenExistingId_whenReadProjectByProjectId_thenReturnsProjects() {
        // given
        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        Project result = service.findProjectByProjectId(projectId);

        // then
        assertEquals(project.getId(), result.getId());
        assertEquals(project.getTitle(), result.getTitle());
        assertEquals(project.getDescription(), result.getDescription());
        assertEquals(project.getImageUrl(), result.getImageUrl());
        assertEquals(project.getStatus(), result.getStatus());
        assertEquals(project.isPrivate(), result.isPrivate());
        assertEquals(project.getEstimatedStartDate(), result.getEstimatedStartDate());
        assertEquals(project.getEstimatedEndDate(), result.getEstimatedEndDate());
        assertEquals(project.getActualStartDate(), result.getActualStartDate());
        assertEquals(project.getActualEndDate(), result.getActualEndDate());

        verify(repository).findById(projectId);
    }

    @DisplayName("유효한 파라미터가 주어진다면_프로젝트를 수정할 때_프로젝트가 갱신돼야 한다.")
    @Test
    void givenValidParameter_whenUpdateProjectByProjectId_thenProjectShouldBeUpdated() {
        // given
        Set<ProjectSkill> existingSkills = new HashSet<>(Set.of(projectSkill1, projectSkill2));
        project.updateSkills(existingSkills);

        List<Long> newSkills = List.of(2L, 3L);
        List<Long> newDisciplines = List.of(2L, 3L);
        ProjectReqUpdateDto dto = ProjectReqUpdateDto.builder()
            .title("테스트_수정_제목_테스트")
            .description("테스트_수정_설명_테스트")
            .imageUrl("테스트_수정_이미지_URL_테스트")
            .status(ProjectStatusType.COMPLETED)
            .isPrivate(true)
            .estimatedStartDate(estimatedStartDate)
            .estimatedEndDate(estimatedEndDate)
            .actualStartDate(actualStartDate)
            .actualEndDate(actualEndDate)
            .skills(newSkills)
            .disciplines(newDisciplines)
            .build();

        ProjectService spy = spy(service);

        when(repository.findById(projectId)).thenReturn(Optional.of(project));
        doNothing().when(spy).addSkillToProject(any(Project.class), anyLong());
        doNothing().when(spy).removeSkillFromProject(any(Project.class), anyLong());
        doNothing().when(spy).addDisciplineToProject(any(Project.class), anyLong());
        doNothing().when(spy).removeDisciplineFromProject(any(Project.class), anyLong());

        // when
        spy.updateProjectByProjectId(projectId, dto);

        // then
        assertEquals("테스트_수정_제목_테스트", project.getTitle());
        assertEquals("테스트_수정_설명_테스트", project.getDescription());
        assertEquals("테스트_수정_이미지_URL_테스트", project.getImageUrl());
        assertEquals(ProjectStatusType.COMPLETED, project.getStatus());
        assertTrue(project.isPrivate());
        verify(repository).findById(projectId);
        verify(spy).addSkillToProject(project, 3L);
        verify(spy).removeSkillFromProject(project, 1L);
        verify(spy).addDisciplineToProject(project, 3L);
        verify(spy).removeDisciplineFromProject(project, 1L);
    }

    @DisplayName("유효한 ID가 주어진다면_프로젝트를 임시 삭제할 때_프로젝트를 삭제된 상태로 갱신해야 한다.")
    @Test
    void givenValidId_whenSoftDeleteProject_thenStatusShouldBeUpdated() {
        when(repository.findById(projectId)).thenReturn(Optional.of(project));

        // when
        service.softDeleteProjectByProjectId(projectId);

        // then
        assertEquals(ProjectStatusType.DELETED, project.getStatus());
        verify(repository).findById(projectId);
    }
}