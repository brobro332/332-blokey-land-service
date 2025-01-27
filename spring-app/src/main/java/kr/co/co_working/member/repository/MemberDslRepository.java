package kr.co.co_working.member.repository;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.team.dto.TeamRequestDto;

import java.util.List;

public interface MemberDslRepository {
    List<MemberResponseDto> readMemberList(MemberRequestDto.READ dto);
    List<MemberResponseDto> readMemberListByTeam(TeamRequestDto.READ dto);
}
