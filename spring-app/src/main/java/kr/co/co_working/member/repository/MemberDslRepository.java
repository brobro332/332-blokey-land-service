package kr.co.co_working.member.repository;

import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;

import java.util.List;

public interface MemberDslRepository {
    List<MemberResponseDto> readMembers(MemberRequestDto.READ dto);
    List<MemberResponseDto> readMembersInWorkspace(WorkspaceRequestDto.READ dto);
    List<MemberResponseDto> readMembersNotInWorkspace(WorkspaceRequestDto.READ dto);
}
