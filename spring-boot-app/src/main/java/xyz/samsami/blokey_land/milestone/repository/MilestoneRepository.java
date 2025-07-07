package xyz.samsami.blokey_land.milestone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    @Query("""
        SELECT new xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto(
            m.id,
            m.title,
            m.description,
            m.dueDate,
            p.id
        )
        FROM Milestone m
        JOIN m.project p
        WHERE p.id = :projectId
    """)
    List<MilestoneRespDto> findDtoByProject(@Param("projectId") Long projectId);
}