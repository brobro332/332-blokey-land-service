package xyz.samsami.blokey_land.blokey.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;

import java.util.UUID;

public interface BlokeyRepository extends JpaRepository<Blokey, UUID> {
    @Query("""
        SELECT new xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto(
            b.id,
            b.nickname,
            b.bio,
            COUNT(o) > 0
        )
        FROM Blokey b
        LEFT JOIN Member m
        ON m.blokey = b
        AND m.project.id = :projectId
        LEFT JOIN Offer o
        ON o.blokey.id = b.id
        AND o.project.id = :projectId
        AND o.status = 'PENDING'
        WHERE m.id IS NULL
        GROUP BY b.id, b.nickname, b.bio
    """)
    Page<BlokeyRespDto> findByNotInProject(@Param("projectId") Long projectId, Pageable pageable);
}