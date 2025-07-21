package xyz.samsami.blokey_land.discipline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.domain.ProjectDiscipline;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;

import java.util.List;
import java.util.Set;

public interface ProjectDisciplineRepository extends JpaRepository<ProjectDiscipline, Long> {
    ProjectDiscipline findByProjectAndDiscipline(Project project, Discipline discipline);

    @Query("""
        SELECT pd
        FROM ProjectDiscipline pd
        JOIN FETCH pd.discipline
        WHERE pd.project.id
        IN :projectIds
    """)
    List<ProjectSkill> findWithDisciplineByProjectIds(@Param("projectIds") Set<Long> projectIds);
}
