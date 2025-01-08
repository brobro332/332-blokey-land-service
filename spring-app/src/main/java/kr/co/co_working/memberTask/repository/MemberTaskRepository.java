package kr.co.co_working.memberTask.repository;

import kr.co.co_working.memberTask.MemberTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTaskRepository extends JpaRepository<MemberTask, Long> {
    List<MemberTask> findByTaskId(Long id);
}