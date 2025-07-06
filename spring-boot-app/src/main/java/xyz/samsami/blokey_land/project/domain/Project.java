package xyz.samsami.blokey_land.project.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import xyz.samsami.blokey_land.common.domain.CommonDateTime;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends CommonDateTime {
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

    public void updateTitle(String title) { if (title != null) this.title = title; }
    public void updateDescription(String description) { if (description != null) this.description = description; }
    public void updateImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void updateStatus(ProjectStatusType status) { if (status != null) this.status = status; }
    public void updateIsPrivate(Boolean isPrivate) { if (isPrivate != null) this.isPrivate = isPrivate; }

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