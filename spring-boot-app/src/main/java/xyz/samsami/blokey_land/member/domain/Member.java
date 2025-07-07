package xyz.samsami.blokey_land.member.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.blokey.domain.Blokey;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends CommonTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blokey_id")
    private Blokey blokey;

    public void updateRole(RoleType role) { if (role != null) this.role = role; }
}