package kr.co.co_working.memberWorkspace.repository;

import kr.co.co_working.memberWorkspace.MemberWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberWorkspaceRepository extends JpaRepository<MemberWorkspace, Long> {
    MemberWorkspace findByMemberEmailAndWorkspaceId(String email, Long workspaceId);
    List<MemberWorkspace> findByWorkspaceId(Long workspaceId);
}
