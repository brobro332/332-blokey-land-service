package kr.co.co_working.task.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.project.repository.entity.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_task")
public class Task extends CommonTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name", nullable = false, length = 20)
    private String name;

    @Column(name = "task_type", nullable = false, length = 10)
    private String type;

    @Column(name = "task_description", nullable = false, length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Task(String name, String type, String description, Project project) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.project = project;
    }

    public void updateTask(String name, String type, String description, Project project) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.project = project;
    }
}