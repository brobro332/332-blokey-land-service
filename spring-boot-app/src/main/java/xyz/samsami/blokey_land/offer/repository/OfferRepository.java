package xyz.samsami.blokey_land.offer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.type.OfferType;

import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query(
            value = """
            SELECT new xyz.samsami.blokey_land.offer.dto.OfferRespDto(
                o.id,
                p.id,
                p.title,
                b.id,
                b.nickname,
                b.bio,
                o.offerer,
                o.status,
                o.createdAt
            )
            FROM Offer o
            JOIN o.project p
            JOIN o.blokey b
            WHERE p.id = :projectId
            AND o.offerer = :offerer
        """,
            countQuery = """
            SELECT count(o)
            FROM Offer o
            JOIN o.project p
            WHERE p.id = :projectId
            AND o.offerer = :offerer
        """
    )
    Page<OfferRespDto> findDtoByProjectId(@Param("projectId") Long projectId, @Param("offerer") OfferType offerer, Pageable pageable);

    @Query(
            value = """
            SELECT new xyz.samsami.blokey_land.offer.dto.OfferRespDto(
                o.id,
                p.id,
                p.title,
                b.id,
                b.nickname,
                b.bio,
                o.offerer,
                o.status,
                o.createdAt
            )
            FROM Offer o
            JOIN o.project p
            JOIN o.blokey b
            WHERE b.id = :blokeyId
            AND o.offerer = :offerer
        """,
            countQuery = """
            SELECT count(o)
            FROM Offer o
            JOIN o.blokey b
            WHERE b.id = :blokeyId
            AND o.offerer = :offerer
        """
    )
    Page<OfferRespDto> findDtoByBlokeyId(@Param("blokeyId") UUID blokeyId, @Param("offerer") OfferType offerer, Pageable pageable);
}