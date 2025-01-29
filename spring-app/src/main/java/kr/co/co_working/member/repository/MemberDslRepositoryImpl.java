package kr.co.co_working.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.dto.QMemberResponseDto;
import kr.co.co_working.team.dto.TeamRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.member.QMember.member;
import static kr.co.co_working.memberTeam.QMemberTeam.memberTeam;
import static kr.co.co_working.team.QTeam.team;

@Repository
@RequiredArgsConstructor
public class MemberDslRepositoryImpl implements MemberDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT member_email, member_name, member_description, member_createdAt, member_modifiedAt
     * FROM tbl_member
     * WHERE member_email = ?
     * AND member_name LIKE ?
     *
     * @param dto
     * @return
     */
    @Override
    public List<MemberResponseDto> readMemberList(MemberRequestDto.READ dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return factory
            .select(
                new QMemberResponseDto(
                    member.email,
                    member.name,
                    member.description,
                    member.createdAt,
                    member.modifiedAt
                )
            )
            .from(member)
            .where(emailEq(email))
            .where(nameContains(dto.getName()))
            .fetch();
    }

    @Override
    public List<MemberResponseDto> readMemberListByTeam(TeamRequestDto.READ dto) {
        return factory
            .select(
                new QMemberResponseDto(
                    member.email,
                    member.name,
                    member.description,
                    member.createdAt,
                    member.modifiedAt,
                    team.leader
                )
            )
            .from(team)
            .join(memberTeam).on(team.id.eq(memberTeam.team.id))
            .join(member).on(member.email.eq(memberTeam.member.email))
            .where(teamIdEq(dto.getId()))
            .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null ? member.email.eq(emailCond) : null;
    }

    private BooleanExpression teamIdEq(Long idCond) {
        return idCond != null ? team.id.eq(idCond) : null;
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? member.name.contains(nameCond) : null;
    }
}