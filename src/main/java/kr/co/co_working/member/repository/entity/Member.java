package kr.co.co_working.member.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.memberTeam.repository.entity.MemberTeam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, String description) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = description;
    }

    public void updateMember(String name, String description) {
        this.name = name;
        this.description = description;
    }
}