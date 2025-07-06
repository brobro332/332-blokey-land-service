package xyz.samsami.blokey_land.task.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.common.domain.CommonDateTime;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.task.type.PriorityType;
import xyz.samsami.blokey_land.task.type.TaskStatusType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Task extends CommonDateTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private UUID assignee;

    @Column
    private Integer progress;

    @Column
    @Enumerated(EnumType.STRING)
    private TaskStatusType status;

    @Column
    @Enumerated(EnumType.STRING)
    private PriorityType priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    public void updateTitle(String title) { if (title != null) this.title = title; }
    public void updateDescription(String description) { if (description != null) this.description = description; }
    public void updateAssignee(UUID assignee) { if (assignee != null) this.assignee = assignee; }
    public void updateProgress(Integer progress) { if (progress != null) this.progress = progress; }
    public void updateStatus(TaskStatusType status) { if (status != null) this.status = status;}
    public void updatePriority(PriorityType priority) { if (priority != null) this.priority = priority; }
    public void updateMilestone(Milestone milestone) { if (milestone != null) this.milestone = milestone; }

    @Builder
    public Task(LocalDate estimatedStartDate, LocalDate estimatedEndDate, LocalDate actualStartDate, LocalDate actualEndDate, String title, String description, UUID assignee, Integer progress, TaskStatusType status, PriorityType priority, Project project, Milestone milestone) {
        super(estimatedStartDate, estimatedEndDate, actualStartDate, actualEndDate);
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.progress = progress;
        this.status = status;
        this.priority = priority;
        this.project = project;
        this.milestone = milestone;
    }
}