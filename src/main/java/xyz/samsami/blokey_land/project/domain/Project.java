package xyz.samsami.blokey_land.project.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends CommonTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    private LocalDate startDate;

    private LocalDate endDate;

    /* TODO: 멤버 도메인 개발
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();
    */
    
    /* TODO: 태스크 도메인 개발
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    */

    /* TODO: 마일스톤 도메인 개발
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Milestone> milestones = new ArrayList<>();
    */

    public void updateTitle(String title) { if (title != null) this.title = title; }

    public void updateDescription(String description) { if (description != null) this.description = description; }

    public void updateOwnerId(UUID ownerId) { if (ownerId != null) this.ownerId = ownerId; }

    public void updateStartDate(LocalDate startDate) { if (startDate != null) this.startDate = startDate; }

    public void updateEndDate(LocalDate endDate) { if (endDate != null) this.endDate = endDate; }

    /* TODO: 멤버 필드 업데이트 메서드
    public void updateMembers(List<Member> members) {
        this.members.clear();
        members.forEach(member -> this.members.add(member));
    }
    */

    /* TODO: 태스크 필드 업데이트 메서드
    public void updateTasks(List<Task> tasks) {
        this.tasks.clear();
        tasks.forEach(task -> this.tasks.add(task));
    }
    */
    
    /* TODO: 마일스톤 필드 업데이트 메서드
    public void updateMilestones(List<Milestone> milestones) {
        this.milestones.clear();
        milestones.forEach(milestone -> this.milestones.add(milestone));
    }
    */
}