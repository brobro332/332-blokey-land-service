package kr.co.co_working.memberTeam.repository;

import kr.co.co_working.memberTeam.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    MemberTeam findByMemberEmailAndTeamId(String email, Long teamId);

    List<MemberTeam> findByMemberEmail(String email);

    List<MemberTeam> findByTeamId(Long teamId);
}
