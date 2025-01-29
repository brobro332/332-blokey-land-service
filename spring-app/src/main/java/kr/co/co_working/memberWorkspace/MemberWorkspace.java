package kr.co.co_working.memberWorkspace;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.member.Member;
import kr.co.co_working.workspace.Workspace;
import lombok.*;

@Entity
@Table(name = "tbl_member_team")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberWorkspace extends CommonTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Workspace workspace;

    @Builder
    public MemberWorkspace(Member member, Workspace workspace) {
        this.member = member;
        this.workspace = workspace;
    }

    public void updateMemberTeam(Member member, Workspace workspace) {
        this.member = member;
        this.workspace = workspace;
    }
}