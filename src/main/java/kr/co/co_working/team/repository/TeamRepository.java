package kr.co.co_working.team.repository;

import kr.co.co_working.team.repository.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
