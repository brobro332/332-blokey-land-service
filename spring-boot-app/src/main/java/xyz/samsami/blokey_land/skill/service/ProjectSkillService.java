package xyz.samsami.blokey_land.skill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.samsami.blokey_land.common.service.AbstractRelationService;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.repository.ProjectSkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectSkillService extends AbstractRelationService<ProjectSkill, Project, Skill> {
    private final ProjectSkillRepository repository;

    @Override
    protected ProjectSkill createRelation(Project project, Skill skill) {
        return ProjectSkill.builder().project(project).skill(skill).build();
    }

    @Override
    protected Optional<ProjectSkill> findRelation(Project project, Skill skill) {
        return Optional.ofNullable(repository.findByProjectAndSkill(project, skill));
    }

    @Override
    protected void saveRelation(ProjectSkill relation) {
        relation.getProject().addSkill(relation);
        repository.save(relation);
    }

    @Override
    protected void deleteRelation(ProjectSkill relation) {
        relation.getProject().removeSkill(relation);
        repository.delete(relation);
    }

    public List<ProjectSkill> findWithSkillByProjectIds(Set<Long> projectIds) {
        return repository.findWithSkillByProjectIds(projectIds);
    }
}
