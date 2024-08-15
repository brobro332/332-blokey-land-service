package kr.co.co_working.team.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.member.repository.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_team")
public class Team extends CommonTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "team_id")
    private Long id;

    @Column(name = "team_name", nullable = false, length = 20)
    private String name;

    @Column(name = "team_description", nullable = false, length = 200)
    private String description;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
    private List<Member> members;

    @Builder
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void insertMember(Member member) {
        this.members.add(member);
        member.updateMember(member.getName(), member.getDescription(), this);
    }

    public void deleteMember(Member member) {
        this.members.remove(member);
    }
}