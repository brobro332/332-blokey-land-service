package xyz.samsami.blokey_land.project.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import xyz.samsami.blokey_land.common.domain.CommonDateTime;

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

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean deleted = false;

    public void updateTitle(String title) { if (title != null) this.title = title; }
    public void updateDescription(String description) { if (description != null) this.description = description; }
    public void updateOwnerId(UUID ownerId) { if (ownerId != null) this.ownerId = ownerId; }
    public void updateDeleted(Boolean deleted) { if (deleted != null) this.deleted = deleted; }

    @Builder
    public Project(
        LocalDate estimatedStartDate,
        LocalDate estimatedEndDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate,
        Long id,
        String title,
        String description,
        UUID ownerId
    ) {
        super(estimatedStartDate, estimatedEndDate, actualStartDate, actualEndDate);
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
    }
}