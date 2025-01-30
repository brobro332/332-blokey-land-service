package kr.co.co_working.invitation;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.member.Member;
import kr.co.co_working.workspace.Workspace;
import lombok.*;

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

    @Column(name = "requester_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RequesterType requesterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = InvitationStatus.PENDING;
        }
    }

    @Builder
    public Invitation(Long id, Workspace workspace, Member member, RequesterType requesterType, InvitationStatus status) {
        this.id = id;
        this.workspace = workspace;
        this.member = member;
        this.requesterType = requesterType;
        this.status = status;
    }
}