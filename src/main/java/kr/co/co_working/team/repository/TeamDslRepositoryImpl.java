package kr.co.co_working.team.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.team.dto.QTeamResponseDto;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.team.repository.entity.QTeam.team;


@Repository
@RequiredArgsConstructor
public class TeamDslRepositoryImpl implements TeamDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT team_id, team_name, team_description, team_createdAt, team_modifiedAt
     * FROM tbl_team
     * WHERE team_name LIKE ?;
     *
     * @param dto
     * @return
     */
    @Override
    public List<TeamResponseDto> readTeamList(TeamRequestDto.READ dto) {
        return factory
                .select(new QTeamResponseDto(team.id, team.name, team.description, team.createdAt, team.modifiedAt))
                .from(team)
                .where(nameContains(dto.getName()))
                .fetch();
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? team.name.contains(nameCond) : null;
    }
}
