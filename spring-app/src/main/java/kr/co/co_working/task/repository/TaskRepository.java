package kr.co.co_working.task.repository;

import kr.co.co_working.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
