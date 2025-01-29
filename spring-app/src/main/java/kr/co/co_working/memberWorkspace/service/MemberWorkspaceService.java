package kr.co.co_working.memberWorkspace.service;

import kr.co.co_working.member.Member;
import kr.co.co_working.memberWorkspace.MemberWorkspace;
import kr.co.co_working.memberWorkspace.repository.MemberWorkspaceRepository;
import kr.co.co_working.workspace.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberWorkspaceService {
    private final MemberWorkspaceRepository repository;

    /**
     * createMemberWorkspace : MemberWorkspace 등록
     * @param member
     * @param workspace
     * @return
     * @throws Exception
     */

    @Transactional
    public Long createMemberWorkspace(Member member, Workspace workspace) throws Exception {
        // 1. MemberWorkspace 빌드
        MemberWorkspace memberWorkspace = MemberWorkspace.builder()
                .member(member)
                .workspace(workspace)
                .build();

        // 2. MemberWorkspace 등록
        repository.save(memberWorkspace);

        // 3. ID 반환
        return memberWorkspace.getId();
    }

    /**
     * deleteMemberWorkspace : MemberWorkspace 삭제
     * @param workspace
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void deleteMemberWorkspace(Member member, Workspace workspace) throws NoSuchElementException, Exception {
        // 1. MemberWorkspace 조회
        MemberWorkspace memberWorkspace = repository.findByMemberEmailAndWorkspaceId(member.getEmail(), workspace.getId());

        // 2. 부재 시 예외 처리
        if (memberWorkspace == null) {
            throw new NoSuchElementException("삭제하려는 멤버-팀 데이터가 존재하지 않습니다.");
        }

        // 3. MemberWorkspace 삭제
        repository.delete(memberWorkspace);
    }

    /**
     * deleteMemberWorkspaceByWorkspaceId : WorkspaceId 기준으로 MemberWorkspace 삭제
     * @param workspace
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void deleteMemberWorkspaceByWorkspaceId(Workspace workspace) throws NoSuchElementException, Exception {
        // 1. MemberWorkspace 조회
        List<MemberWorkspace> memberWorkspaceList = repository.findByWorkspaceId(workspace.getId());

        // 2. 부재 시 예외 처리
        if (memberWorkspaceList.size() == 0) {
            throw new NoSuchElementException("삭제하려는 멤버-팀 데이터가 존재하지 않습니다.");
        }

        // 3. MemberWorkspace 삭제
        for (MemberWorkspace memberWorkspace : memberWorkspaceList) {
            repository.delete(memberWorkspace);
        }
    }
}