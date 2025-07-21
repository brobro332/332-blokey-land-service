package xyz.samsami.blokey_land.project.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
    SELECT new xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto(
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
    List<ProjectOnlyRespDto> findProjectsByBlokeyId(@Param("blokeyId") UUID blokeyId);

    @Query("""
        SELECT DISTINCT p
        FROM Member m
        JOIN m.project p
        LEFT JOIN FETCH p.tasks t
        WHERE m.blokey.id = :blokeyId
    """)
    List<Project> findProjectsWithTasksByBlokeyId(@Param("blokeyId") UUID blokeyId);

    @EntityGraph(attributePaths = {"skills", "disciplines"})
    @NonNull Optional<Project> findById(@NonNull Long projectId);
}