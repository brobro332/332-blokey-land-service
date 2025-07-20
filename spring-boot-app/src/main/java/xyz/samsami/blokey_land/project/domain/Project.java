package xyz.samsami.blokey_land.project.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import xyz.samsami.blokey_land.common.domain.CommonDateTime;
import xyz.samsami.blokey_land.discipline.domain.ProjectDiscipline;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.common.domain.RelationTarget;
import xyz.samsami.blokey_land.task.domain.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends CommonDateTime implements RelationTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String imageUrl;

    @Column
    @Enumerated(EnumType.STRING)
    private ProjectStatusType status;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isPrivate;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ProjectSkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<ProjectDiscipline> disciplines = new HashSet<>();

    public void addTask(Task task) {
        tasks.add(task);
        task.updateProject(this);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.updateProject(null);
    }

    public void addSkill(ProjectSkill projectSkill) {
        skills.add(projectSkill);
        projectSkill.updateProject(this);
    }

    public void removeSkill(ProjectSkill projectSkill) {
        skills.remove(projectSkill);
        projectSkill.updateProject(null);
    }

    public void addDiscipline(ProjectDiscipline projectDiscipline) {
        disciplines.add(projectDiscipline);
        projectDiscipline.updateProject(this);
    }

    public void removeDiscipline(ProjectDiscipline projectDiscipline) {
        disciplines.remove(projectDiscipline);
        projectDiscipline.updateProject(null);
    }

    public void updateTitle(String title) { if (title != null) this.title = title; }
    public void updateDescription(String description) { if (description != null) this.description = description; }
    public void updateImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void updateStatus(ProjectStatusType status) { if (status != null) this.status = status; }
    public void updateIsPrivate(Boolean isPrivate) { if (isPrivate != null) this.isPrivate = isPrivate; }
    public void updateSkills(Set<ProjectSkill> skills) { if (skills != null) this.skills = skills; }

    @Builder
    public Project(
        LocalDate estimatedStartDate,
        LocalDate estimatedEndDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate,
        Long id,
        String title,
        String description,
        String imageUrl,
        ProjectStatusType status,
        boolean isPrivate
    ) {
        super(estimatedStartDate, estimatedEndDate, actualStartDate, actualEndDate);
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.isPrivate = isPrivate;
    }
}