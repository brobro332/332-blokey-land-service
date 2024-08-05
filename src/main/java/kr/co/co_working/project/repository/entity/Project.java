package kr.co.co_working.project.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.task.repository.entity.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_project")
public class Project extends CommonTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "project_name", nullable = false, length = 20)
    private String name;

    @Column(name = "project_description", nullable = false, length = 200)
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @Builder
    public Project(Long id, String name, String description, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tasks = tasks;
    }
}