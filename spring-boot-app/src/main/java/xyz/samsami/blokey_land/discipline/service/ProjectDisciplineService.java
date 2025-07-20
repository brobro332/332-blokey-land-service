package xyz.samsami.blokey_land.discipline.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.samsami.blokey_land.common.service.AbstractRelationService;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.domain.ProjectDiscipline;
import xyz.samsami.blokey_land.discipline.repository.ProjectDisciplineRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectDisciplineService extends AbstractRelationService<ProjectDiscipline, Project, Discipline> {
    private final ProjectDisciplineRepository repository;

    @Override
    protected ProjectDiscipline createRelation(Project project, Discipline Discipline) {
        return ProjectDiscipline.builder().project(project).discipline(Discipline).build();
    }

    @Override
    protected Optional<ProjectDiscipline> findRelation(Project blokey, Discipline Discipline) {
        return Optional.ofNullable(repository.findByProjectAndDiscipline(blokey, Discipline));
    }

    @Override
    protected void saveRelation(ProjectDiscipline relation) {
        relation.getProject().addDiscipline(relation);
        repository.save(relation);
    }

    @Override
    protected void deleteRelation(ProjectDiscipline relation) {
        relation.getProject().removeDiscipline(relation);
        repository.delete(relation);
    }

    public List<ProjectSkill> findWithDisciplineByProjectIds(Set<Long> projectIds) {
        return repository.findWithDisciplineByProjectIds(projectIds);
    }
}
