package kr.co.co_working.memberTeam.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.member.repository.entity.Member;
import kr.co.co_working.team.repository.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_member_team")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTeam extends CommonTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_team_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public MemberTeam(Member member, Team team) {
        this.member = member;
        this.team = team;
    }

    public void updateMemberTeam(Member member, Team team) {
        this.member = member;
        this.team = team;
    }
}