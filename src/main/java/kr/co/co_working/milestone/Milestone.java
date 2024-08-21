package kr.co.co_working.milestone;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.project.Project;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_milestone")
public class Milestone extends CommonTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "milestone_id")
    private Long id;

    @Column(name = "milestone_name", nullable = false, length = 20)
    private String name;

    @Column(name = "milestone_description", nullable = false, length = 200)
    private String description;

    @Column(name = "milestone_dueAt", nullable = false)
    private LocalDateTime dueAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Project project;
}
