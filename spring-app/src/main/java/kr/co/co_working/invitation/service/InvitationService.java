package kr.co.co_working.invitation.service;

import ch.qos.logback.core.util.StringUtil;
import jakarta.transaction.Transactional;
import kr.co.co_working.invitation.Invitation;
import kr.co.co_working.invitation.InvitationStatus;
import kr.co.co_working.invitation.RequesterType;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.dto.InvitationResponseDto;
import kr.co.co_working.invitation.repository.InvitationDslRepository;
import kr.co.co_working.invitation.repository.InvitationRepository;
import kr.co.co_working.member.Member;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.memberWorkspace.service.MemberWorkspaceService;
import kr.co.co_working.workspace.Workspace;
import kr.co.co_working.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository repository;
    private final InvitationDslRepository dslRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final MemberWorkspaceService memberWorkspaceService;

    /**
     * createInvitation : Invitation 생성
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    public Long createInvitation(InvitationRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. 토큰 이메일 추출
        if (RequesterType.MEMBER.name().equals(dto.getRequesterType())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            dto.setMemberEmail(email);
        }

        // 2. Workspace 존재 여부 확인
        Workspace workSpace = workspaceRepository.findById(dto.getWorkspaceId()).orElseThrow(
            () -> new NoSuchElementException("해당 워크스페이스가 존재하지 않습니다.")
        );

        // 3. Member 존재 여부 확인
        Member member = memberRepository.findById(dto.getMemberEmail()).orElseThrow(
            () -> new NoSuchElementException("해당 멤버가 존재하지 않습니다.")
        );

        // 4. Invitation 생성
        Invitation invitation = Invitation.builder()
            .workspace(workSpace)
            .member(member)
            .requesterType((dto.getRequesterType().equals(RequesterType.WORKSPACE.name()) ? RequesterType.WORKSPACE : RequesterType.MEMBER))
            .build();

        // 5. Invitation 등록
        return repository.save(invitation).getId();
    }

    /**
     * readInvitation : Invitation 조회
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public List<InvitationResponseDto> readInvitation(InvitationRequestDto.READ dto) throws NoSuchElementException, Exception {
        return dslRepository.readInvitationList(dto);
    }

    /**
     * updateInvitation : Invitation 수정
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void updateInvitation(InvitationRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. 토큰 이메일 추출
        if (StringUtil.isNullOrEmpty(dto.getMemberEmail())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            dto.setMemberEmail(email);
        }

        // 2. Member 조회
        Member member = memberRepository.findById(dto.getMemberEmail()).orElseThrow(
            () -> new NoSuchElementException("해당 멤버가 존재하지 않습니다.")
        );

        // 3. Workspace 조회
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId()).orElseThrow(
            () -> new NoSuchElementException("해당 워크스페이스가 존재하지 않습니다.")
        );

        // 4. Invitation 조회
        Invitation invitation = repository.findById(dto.getId()).orElseThrow(
            () -> new NoSuchElementException("해당 가입요청이 존재하지 않습니다.")
        );

        // 5. InvitationStatus 값이 수락일 경우 Member -> Workspace 추가
        if (InvitationStatus.ACCEPTED.name().equals(dto.getStatus())) {
            memberWorkspaceService.createMemberWorkspace(member, workspace);
        }

        // 6. Invitation 수정
        invitation.updateInvitation(InvitationStatus.ACCEPTED.name().equals(dto.getStatus()) ? InvitationStatus.ACCEPTED : InvitationStatus.REJECTED);
    }

    /**
     * deleteInvitation : Invitation 삭제
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void deleteInvitation(InvitationRequestDto.DELETE dto) throws NoSuchElementException, Exception {
        // 1. Invitation 존재 여부 확인
        Invitation invitation = repository.findById(dto.getId()).orElseThrow(
            () -> new NoSuchElementException("해당 가입요청이 존재하지 않습니다.")
        );

        // 2. Invitation 삭제
        repository.delete(invitation);
    }
}
