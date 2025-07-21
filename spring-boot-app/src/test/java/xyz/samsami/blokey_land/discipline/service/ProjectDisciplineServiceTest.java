package xyz.samsami.blokey_land.discipline.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.domain.ProjectDiscipline;
import xyz.samsami.blokey_land.discipline.repository.ProjectDisciplineRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectDisciplineServiceTest {
    @InjectMocks private ProjectDisciplineService service;
    @Mock private ProjectDisciplineRepository repository;

    private Project project;
    private Discipline discipline;
    private ProjectDiscipline relation;

    @BeforeEach
    void setUp() {
        project = Project.builder().id(1L).build();
        discipline = Discipline.builder().id(1L).name("프론트엔드 개발").build();
        relation = ProjectDiscipline.builder().project(project).discipline(discipline).build();
    }

    @Test
    @DisplayName("프로젝트와 분야가 주어진다면_연관관계를 생성할 때_올바른 객체가 생성돼야 한다.")
    void givenProjectAndDiscipline_whenCreateRelation_thenReturnsCorrectRelation() {
        // when
        ProjectDiscipline projectDiscipline = service.createRelation(project, discipline);

        // then
        assertEquals(project, projectDiscipline.getProject());
        assertEquals(discipline, projectDiscipline.getDiscipline());
    }

    @Test
    @DisplayName("존재하는 연관관계가 주어진다면_연관관계를 조회할 때_결과가 반환돼야 한다.")
    void givenExistingRelation_whenFindRelation_thenReturnsOptional() {
        // given
        when(repository.findByProjectAndDiscipline(project, discipline)).thenReturn(relation);

        // when
        Optional<ProjectDiscipline> result = service.findRelation(project, discipline);

        // then
        assertTrue(result.isPresent());
        assertEquals(relation, result.get());
    }

    @Test
    @DisplayName("연관관계가 존재하지 않는다면_연관관계를 조회할 때_결과가 없어야 한다.")
    void givenNoRelation_whenFindRelation_thenReturnEmptyOptional() {
        // given
        when(repository.findByProjectAndDiscipline(project, discipline)).thenReturn(null);

        // when
        Optional<ProjectDiscipline> result = service.findRelation(project, discipline);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("연관관계가 주어진다면_연관관계를 저장할 때_올바르게 저장돼야 한다.")
    void givenRelation_whenSaveRelation_thenShouldAddToProjectAndSave() {
        // given
        Project spyProject = spy(project);
        ProjectDiscipline relation = ProjectDiscipline.builder()
            .project(spyProject)
            .discipline(discipline)
            .build();

        // when
        service.saveRelation(relation);

        // then
        verify(spyProject).addDiscipline(relation);
        verify(repository).save(relation);
    }

    @Test
    @DisplayName("연관관계가 주어진다면_연관관계를 삭제할 때_올바르게 제거돼야 한다.")
    void givenRelation_whenDeleteRelation_thenShouldRemoveFromProjectAndDelete() {
        // given
        Project spyProject = spy(project);
        ProjectDiscipline relation = ProjectDiscipline.builder()
            .project(spyProject)
            .discipline(discipline)
            .build();

        // when
        service.deleteRelation(relation);

        // then
        verify(spyProject).removeDiscipline(relation);
        verify(repository).delete(relation);
    }

    @Test
    @DisplayName("프로젝트 ID 목록이 주어진다면_연관관계와 함께 관련 분야를 조회할 때_결과가 반환돼야 한다.")
    void givenProjectIds_whenFindWithDiscipline_thenReturnProjectSkillList() {
        // given
        Set<Long> projectIds = Set.of(1L, 2L);
        List<ProjectSkill> mock = List.of(mock(ProjectSkill.class));
        when(repository.findWithDisciplineByProjectIds(projectIds)).thenReturn(mock);

        // when
        List<ProjectSkill> result = service.findWithDisciplineByProjectIds(projectIds);

        // then
        assertEquals(mock, result);
        verify(repository).findWithDisciplineByProjectIds(projectIds);
    }
}
