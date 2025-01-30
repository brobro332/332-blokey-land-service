package kr.co.co_working.invitation;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.member.Member;
import kr.co.co_working.workspace.Workspace;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "tbl_invitation")
public class Invitation extends CommonTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "invitation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = InvitationStatus.PENDING;
        }
    }
}
