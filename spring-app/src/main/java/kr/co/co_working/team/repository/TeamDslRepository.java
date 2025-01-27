package kr.co.co_working.team.repository;

import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.team.dto.TeamRequestDto;
import kr.co.co_working.team.dto.TeamResponseDto;

import java.util.List;

public interface TeamDslRepository {
    List<TeamResponseDto> readTeamList(TeamRequestDto.READ dto);

    List<MemberResponseDto> readMemberListByTeam(TeamRequestDto.READ dto);
}
