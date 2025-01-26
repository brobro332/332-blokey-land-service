package kr.co.co_working.team.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.team.dto.QTeamResponseDto;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.team.QTeam.team;
import static kr.co.co_working.member.QMember.member;
import static kr.co.co_working.memberTeam.QMemberTeam.memberTeam;

@Repository
@RequiredArgsConstructor
public class TeamDslRepositoryImpl implements TeamDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT team_id, team_name, team_description, team_createdAt, team_modifiedAt, member_email, member_name
     * FROM tbl_team t
     * JOIN tbl_memberTeam mt
     * ON t.team_id = mt.team_id
     * JOIN tbl_member m
     * ON mt.member_email = m.member_email
     * WHERE email = ?;
     *
     * @param dto
     * @return
     */
    @Override
    public List<TeamResponseDto> readTeamList(TeamRequestDto.READ dto) {
        return factory
            .select(
                new QTeamResponseDto(
                    team.id,
                    team.name,
                    team.description,
                    team.createdAt,
                    team.modifiedAt,
                    member.email,
                    member.name
                )
            )
            .from(team)
            .join(memberTeam).on(team.id.eq(memberTeam.team.id))
            .join(member).on(member.email.eq(memberTeam.member.email))
            .where(emailEq(dto.getEmail()))
            .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null ? member.email.eq(emailCond) : null;
    }
}