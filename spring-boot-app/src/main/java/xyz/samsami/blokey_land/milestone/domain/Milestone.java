package xyz.samsami.blokey_land.milestone.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;
import xyz.samsami.blokey_land.project.domain.Project;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Milestone extends CommonTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void updateTitle(String title) { if (title != null) this.title = title; }
    public void updateDescription(String description) { if (description != null) this.description = description; }
    public void updateDueDate(LocalDate dueDate) { if (dueDate != null) this.dueDate = dueDate; }
}