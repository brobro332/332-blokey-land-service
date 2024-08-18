package kr.co.co_working.team.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import kr.co.co_working.memberTeam.repository.entity.MemberTeam;
import kr.co.co_working.project.repository.entity.Project;
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

    @Column(name = "team_description", nullable = false, length = 200)
    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<MemberTeam> memberTeams = new ArrayList<>();

    @Builder
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void insertProject(Project project) {
        this.projects.add(project);
        project.updateProject(project.getName(), project.getDescription(), this);
    }
}