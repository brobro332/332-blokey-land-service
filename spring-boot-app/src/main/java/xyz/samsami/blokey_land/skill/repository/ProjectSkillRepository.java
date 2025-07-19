package xyz.samsami.blokey_land.skill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.skill.domain.Skill;

import java.util.List;
import java.util.Set;

public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {
    ProjectSkill findByProjectAndSkill(Project project, Skill skill);
    @Query("""
        SELECT ps FROM ProjectSkill ps
        JOIN FETCH ps.skill
        WHERE ps.project.id
        IN :projectIds
    """)
    List<ProjectSkill> findWithSkillByProjectIds(@Param("projectIds") Set<Long> projectIds);
}
