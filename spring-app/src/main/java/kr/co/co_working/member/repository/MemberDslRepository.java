package kr.co.co_working.member.repository;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;

import java.util.List;

public interface MemberDslRepository {
    List<MemberResponseDto> readMemberList(MemberRequestDto.READ dto);
    List<MemberResponseDto> readMemberListInWorkspace(MemberRequestDto.READ dto);
    List<MemberResponseDto> readMemberListNotInWorkspace(MemberRequestDto.READ dto);
}
