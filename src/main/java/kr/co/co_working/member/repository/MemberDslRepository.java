package kr.co.co_working.member.repository;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;

import java.util.List;

public interface MemberDslRepository {
    List<MemberResponseDto> readMemberList(MemberRequestDto.READ dto);
}
