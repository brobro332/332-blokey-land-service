package xyz.samsami.blokey_land.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.dto.MemberRespDto;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.blokey.domain.Blokey;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(
        value = """
            SELECT new xyz.samsami.blokey_land.member.dto.MemberRespDto(
                m.id,
                p.id,
                m.role,
                b.nickname,
                b.bio
            )
            FROM Member m
            JOIN m.project p
            JOIN m.blokey b
            WHERE p.id = :projectId
        """,
        countQuery = """
            SELECT count(m)
            FROM Member m
            JOIN m.project p
            WHERE p.id = :projectId
        """
    )
    Page<MemberRespDto> findDtoByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    @Query(
            value = """
            SELECT new xyz.samsami.blokey_land.member.dto.MemberRespDto(
                m.id,
                p.id,
                m.role,
                b.nickname,
                b.bio
            )
            FROM Member m
            JOIN m.project p
            JOIN m.blokey b
            WHERE b.id = :blokeyId
        """,
            countQuery = """
            SELECT count(m)
            FROM Member m
            JOIN m.blokey b
            WHERE b.id = :blokeyId
        """
    )
    Page<MemberRespDto> findDtoByBlokeyId(@Param("blokeyId") UUID blokeyId, Pageable pageable);
}