package kr.co.co_working.workspace.service;

import kr.co.co_working.member.Member;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.memberWorkspace.service.MemberWorkspaceService;
import kr.co.co_working.workspace.Workspace;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import kr.co.co_working.workspace.dto.WorkspaceResponseDto;
import kr.co.co_working.workspace.repository.WorkspaceDslRepository;
import kr.co.co_working.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final MemberWorkspaceService memberWorkspaceService;
    private final WorkspaceRepository repository;
    private final MemberRepository memberRepository;
    private final WorkspaceDslRepository dslRepository;

    /**
     * createWorkspace : Workspace 등록
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public Long createWorkspace(WorkspaceRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = memberRepository.findById(email);

        // 2. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("등록하려는 멤버가 존재하지 않습니다.");
        }

        // 3. Member 추출
        Member member = selectedMember.get();

        // 4. Team 빌드
        Workspace workspace = Workspace.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .leader(email)
            .build();

        // 5. Team, MemberTeam 등록
        repository.save(workspace);
        memberWorkspaceService.createMemberWorkspace(member, workspace);

        // 6. ID 반환
        return workspace.getId();
    }

    /**
     * readWorkspaceList : WorkspaceList 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<WorkspaceResponseDto> readWorkspaceList(WorkspaceRequestDto.READ dto) throws Exception {
        // QueryDSL 동적 쿼리 결과 반환
        return dslRepository.readWorkspaceList(dto);
    }

    /**
     * readWorkspaceListNotJoined : 미가입 WorkspaceList 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<WorkspaceResponseDto> readWorkspaceListNotJoined(WorkspaceRequestDto.READ dto) throws Exception {
        // QueryDSL 동적 쿼리 결과 반환
        return dslRepository.readWorkspaceListNotJoined(dto);
    }

    /**
     * updateWorkspace : Workspace 수정
     * @param dto
     * @return
     * @throws Exception
     */
    @Transactional
    public void updateWorkspace(Long id, WorkspaceRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. Team 조회
        Optional<Workspace> selectedWorkspace = repository.findById(id);

        // 2. 부재 시 예외 처리
        if (selectedWorkspace.isEmpty()) {
            throw new NoSuchElementException("수정하려는 팀이 존재하지 않습니다. ID : " + id);
        }

        // 3. 존재 시 수정 처리
        Workspace workspace = selectedWorkspace.get();
        workspace.updateWorkspace(dto.getName(), dto.getDescription());
    }

    /**
     * deleteWorkspace : Workspace 삭제
     * @param id
     * @throws Exception
     */
    @Transactional
    public void deleteWorkspace(Long id) throws NoSuchElementException, Exception {
        // 1. Team 조회
        Optional<Workspace> selectedWorkspace = repository.findById(id);

        // 2. 부재 시 예외 처리
        if (selectedWorkspace.isEmpty()) {
            throw new NoSuchElementException("삭제하려는 팀이 존재하지 않습니다. ID : " + id);
        }

        // 3. Team 추출
        Workspace workspace = selectedWorkspace.get();

        // 4. 존재 시 삭제 처리
        memberWorkspaceService.deleteMemberWorkspaceByWorkspaceId(workspace);
        repository.delete(workspace);
    }

    @Transactional
    public void addMemberToWorkspace(Long teamId, String email) throws NoSuchElementException, Exception {
        // 1. Workspace 조회
        Optional<Workspace> selectedWorkspace = repository.findById(teamId);

        // 2. 부재 시 예외 처리
        if (selectedWorkspace.isEmpty()) {
            throw new NoSuchElementException("해당 팀이 존재하지 않습니다. ID : " + teamId);
        }

        // 3. Member 조회
        Optional<Member> selectedMember = memberRepository.findById(email);

        // 4. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("추가하려는 멤버가 존재하지 않습니다. Email : " + email);
        }

        // 5. 객체 추출
        Member member = selectedMember.get();
        Workspace workspace = selectedWorkspace.get();

        // 6. MemberWorkspace 등록
        memberWorkspaceService.createMemberWorkspace(member, workspace);
    }

    @Transactional
    public void removeMemberFromWorkspace(String email, Long teamId) throws NoSuchElementException, Exception {
        // 1. Team 조회
        Optional<Workspace> selectedWorkspace = repository.findById(teamId);

        // 2. 부재 시 예외 처리
        if (selectedWorkspace.isEmpty()) {
            throw new NoSuchElementException("해당 팀이 존재하지 않습니다. ID : " + teamId);
        }

        // 3. Member 조회
        Optional<Member> selectedMember = memberRepository.findById(email);

        // 4. 부재 시 예외 처리
        if (selectedMember.isEmpty()) {
            throw new NoSuchElementException("제외하려는 멤버가 존재하지 않습니다. Email : " + email);
        }

        // 5. 객체 추출
        Member member = selectedMember.get();
        Workspace workspace = selectedWorkspace.get();

        // 7. MemberTeam 삭제
        memberWorkspaceService.deleteMemberWorkspace(member, workspace);
    }
}