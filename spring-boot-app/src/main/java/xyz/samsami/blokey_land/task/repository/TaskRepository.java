package xyz.samsami.blokey_land.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.task.domain.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("""
        SELECT t
        FROM Task t
        JOIN FETCH t.project
        WHERE t.project.id = :projectId
    """)
    List<Task> findAllByProjectId(@Param("projectId") Long projectId);
}