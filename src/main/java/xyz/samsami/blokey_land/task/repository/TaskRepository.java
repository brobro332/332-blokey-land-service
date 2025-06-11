package xyz.samsami.blokey_land.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.task.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying
    @Query("""
        UPDATE Task t
        SET t.milestone = null
        WHERE t.milestone = :milestone
    """)
    void clearMilestoneFromTasks(@Param("milestone") Milestone milestone);
}