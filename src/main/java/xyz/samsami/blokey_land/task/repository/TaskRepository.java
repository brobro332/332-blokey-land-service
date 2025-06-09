package xyz.samsami.blokey_land.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.task.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}