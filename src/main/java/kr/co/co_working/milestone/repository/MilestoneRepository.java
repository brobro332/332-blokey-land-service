package kr.co.co_working.milestone.repository;

import kr.co.co_working.milestone.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}