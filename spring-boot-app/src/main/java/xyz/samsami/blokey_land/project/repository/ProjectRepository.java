package xyz.samsami.blokey_land.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
        SELECT DISTINCT m.project
        FROM Member m
        WHERE m.blokey.id = :blokeyId
    """)
    List<Project> findProjectsByBlokeyId(@Param("blokeyId") UUID blokeyId);

    @Query("""
    SELECT new xyz.samsami.blokey_land.project.dto.ProjectRespDto(
        p.id,
        p.title,
        p.description,
        p.imageUrl,
        p.status,
        p.isPrivate,
        CASE WHEN m.role = 'LEADER' THEN true ELSE false END,
        p.estimatedStartDate,
        p.estimatedEndDate,
        p.actualStartDate,
        p.actualEndDate
    )
    FROM Member m
    JOIN m.project p
    WHERE m.blokey.id = :blokeyId
    """)
    List<ProjectRespDto> findProjectsWithRoleByBlokeyId(@Param("blokeyId") UUID blokeyId);
}