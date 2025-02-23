package kr.co.co_working.invitation.repository;

import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.dto.InvitationResponseDto;

import java.util.List;

public interface InvitationDslRepository {
    List<InvitationResponseDto> readInvitationList(InvitationRequestDto.READ dto);
}