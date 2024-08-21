package kr.co.co_working.memberTeam.repository;

import kr.co.co_working.memberTeam.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    MemberTeam findByMemberEmailAndTeamId(String email, Long teamId);

    MemberTeam findByTeamId(Long teamId);
}
