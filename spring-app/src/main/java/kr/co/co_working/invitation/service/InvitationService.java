package kr.co.co_working.invitation.service;

import kr.co.co_working.invitation.Invitation;
import kr.co.co_working.invitation.RequesterType;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.dto.InvitationResponseDto;
import kr.co.co_working.invitation.repository.InvitationDslRepository;
import kr.co.co_working.invitation.repository.InvitationRepository;
import kr.co.co_working.member.Member;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.workspace.Workspace;
import kr.co.co_working.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
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

    /**
     * createInvitation : 워크스페이스 가입요청 생성
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    public Long createInvitation(InvitationRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. 워크스페이스 존재 여부 확인
        Workspace workSpace = workspaceRepository.findById(dto.getWorkspaceId()).orElseThrow(
            () -> new NoSuchElementException("해당 워크스페이스가 존재하지 않습니다.")
        );

        // 2. 멤버 존재 여부 확인
        Member member = memberRepository.findById(dto.getMemberId()).orElseThrow(
            () -> new NoSuchElementException("해당 멤버가 존재하지 않습니다.")
        );

        // 3. 가입요청 생성
        Invitation invitation = Invitation.builder()
            .workspace(workSpace)
            .member(member)
            .requesterType((dto.getRequesterType().equals(RequesterType.WORKSPACE.name()) ? RequesterType.WORKSPACE : RequesterType.MEMBER))
            .build();

        // 4. 가입요청 등록
        return repository.save(invitation).getId();
    }

    /**
     * readInvitation : 워크스페이스 가입요청 조회
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public List<InvitationResponseDto> readInvitation(InvitationRequestDto.READ dto) throws NoSuchElementException, Exception {
        return dslRepository.readInvitationList(dto);
    }

    /**
     * deleteInvitation : 워크스페이스 가입요청 삭제
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void deleteInvitation(InvitationRequestDto.DELETE dto) throws NoSuchElementException, Exception {
        // 1. 가입요청 존재 여부 확인
        Invitation invitation = repository.findById(dto.getId()).orElseThrow(
            () -> new NoSuchElementException("해당 가입요청이 존재하지 않습니다.")
        );

        // 2. 가입요청 삭제
        repository.delete(invitation);
    }
}
