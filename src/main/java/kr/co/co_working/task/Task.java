package kr.co.co_working.task;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.member.Member;
import kr.co.co_working.memberTask.MemberTask;
import kr.co.co_working.project.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "task_description", length = 200)
    private String description;

    @Column(name = "task_start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "task_end_at", nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "task")
    private List<MemberTask> memberTasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public Task(String name, String type, String description, Project project, LocalDateTime startAt, LocalDateTime endAt) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.project = project;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void updateTask(String name, String type, String description, Project project, LocalDateTime startAt, LocalDateTime endAt) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.project = project;
        this.startAt = startAt;
        this.endAt = endAt;
    }
}