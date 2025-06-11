package xyz.samsami.blokey_land.milestone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    @Query("""
        SELECT new xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto(
            m.id,
            p.id,
            m.title,
            m.description,
            m.dueDate
        )
        FROM Milestone m
        JOIN m.project p
        WHERE p.id = :projectId
    """)
    Page<MilestoneRespDto> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);
}