package kr.co.co_working.member.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.team.repository.entity.Team;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_member")
public class Member extends CommonTime {
    @Id
    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false, length = 10)
    private String password;

    @Column(name = "member_name", nullable = false, length = 20)
    private String name;

    @Column(name = "member_description", nullable = false, length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    public Member(String email, String password, String name, String description, Team team) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = description;
        this.team = team;
    }

    public void updateMember(String name, String description, Team team) {
        this.name = name;
        this.description = description;
        this.team = team;
    }
}