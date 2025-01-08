package kr.co.co_working.team;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.memberTeam.MemberTeam;
import kr.co.co_working.project.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @Column(name = "team_description", length = 200)
    private String description;

    @Column(name = "leader_email")
    private String leader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @Builder
    public Team(String name, String description, String leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }

    public void updateTeam(String name, String description, String leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }

    public void insertProject(Project project) {
        this.projects.add(project);
        project.updateProject(project.getName(), project.getDescription());
    }
}