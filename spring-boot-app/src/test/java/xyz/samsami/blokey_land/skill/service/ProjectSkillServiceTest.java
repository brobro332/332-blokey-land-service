package xyz.samsami.blokey_land.skill.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.repository.ProjectSkillRepository;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectSkillServiceTest {
    @InjectMocks ProjectSkillService projectSkillService;
    @Mock ProjectSkillRepository projectSkillRepository;

    Project project;
    Skill skill;

    @BeforeEach
    void setUp() {
        project = Project.builder().id(1L).build();
        skill = Skill.builder().id(1L).build();
    }

    @Test
    @DisplayName("프로젝트와 스킬이 주어진다면_연관관계를 생성할 때_올바르게 저장해야 한다.")
    void givenProjectAndSkill_whenCreateRelation_thenSaveNewRelation() {
        // given
        when(projectSkillRepository.findByProjectAndSkill(project, skill)).thenReturn(null);

        // when
        projectSkillService.create(project, skill);

        // then
        verify(projectSkillRepository).save(argThat(saved ->
            saved.getProject().equals(project) && saved.getSkill().equals(skill)
        ));
    }

    @Test
    @DisplayName("이미 존재하는 프로젝트와 스킬이 주어진다면_연관관계를 생성할 때_연관관계를 생성하지 않는다.")
    void givenExistingRelation_whenCreateRelation_thenDoNothing() {
        // given
        ProjectSkill existingRelation = ProjectSkill.builder().project(project).skill(skill).build();

        when(projectSkillRepository.findByProjectAndSkill(project, skill)).thenReturn(existingRelation);

        // when
        projectSkillService.create(project, skill);

        // then
        verify(projectSkillRepository, never()).save(any());
    }

    @Test
    @DisplayName("존재하는 연관관계가 주어진다면_연관관계를 삭제할 때_올바르게 삭제해야 한다.")
    void givenExistingRelation_whenDeleteRelation_thenCallsMethod() {
        // given
        ProjectSkill relation = ProjectSkill.builder().project(project).skill(skill).build();

        when(projectSkillRepository.findByProjectAndSkill(project, skill)).thenReturn(relation);

        // when
        projectSkillService.delete(project, skill);

        // then
        verify(projectSkillRepository).delete(relation);
    }

    @Test
    @DisplayName("존재하지 않는 연관관계가 주어진다면_연관관계를 삭제할 때_예외가 발생해야 한다.")
    void givenNonExistingRelation_whenDeleteRelation_thenThrowsException() {
        // given
        when(projectSkillRepository.findByProjectAndSkill(project, skill)).thenReturn(null);

        // when, then
        assertThrows(CommonException.class, () -> projectSkillService.delete(project, skill));
    }
}