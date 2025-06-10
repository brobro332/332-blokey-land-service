package xyz.samsami.blokey_land.offer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;

import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query(
            value = """
            SELECT new xyz.samsami.blokey_land.offer.dto.OfferRespDto(
                o.id,
                o.role,
                p.id,
                u.id
            )
            FROM Offer o
            JOIN o.project p
            JOIN o.user u
            WHERE p.id = :projectId
        """,
            countQuery = """
            SELECT count(o)
            FROM Offer o
            JOIN o.project p
            WHERE p.id = :projectId
        """
    )
    Page<OfferRespDto> findDtoByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @Query(
            value = """
            SELECT new xyz.samsami.blokey_land.offer.dto.OfferRespDto(
                o.id,
                o.role,
                p.id,
                u.id
            )
            FROM Offer o
            JOIN o.project p
            JOIN o.user u
            WHERE u.id = :userId
        """,
            countQuery = """
            SELECT count(o)
            FROM Offer o
            JOIN o.project p
            WHERE u.id = :userId
        """
    )
    Page<OfferRespDto> findDtoByUserId(@Param("userId") UUID userId, Pageable pageable);
}