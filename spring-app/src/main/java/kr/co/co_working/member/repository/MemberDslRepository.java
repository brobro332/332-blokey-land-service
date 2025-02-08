package kr.co.co_working.member.repository;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;

import java.util.List;

public interface MemberDslRepository {
    List<MemberResponseDto> readMembers(MemberRequestDto.READ dto);
    List<MemberResponseDto> readMemberListInWorkspace(WorkspaceRequestDto.READ dto);
    List<MemberResponseDto> readMemberListNotInWorkspace(WorkspaceRequestDto.READ dto);
}
