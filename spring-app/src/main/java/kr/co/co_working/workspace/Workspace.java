package kr.co.co_working.workspace;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.memberWorkspace.MemberWorkspace;
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
@Table(name = "tbl_workspace")
public class Workspace extends CommonTime {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "workspace_id")
    private Long id;

    @Column(name = "workspace_name", nullable = false, length = 20)
    private String name;

    @Column(name = "workspace_description", length = 200)
    private String description;

    @Column(name = "leader_email")
    private String leader;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<MemberWorkspace> memberWorkspaces = new ArrayList<>();

    @Builder
    public Workspace(String name, String description, String leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
    }

    public void updateWorkspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void insertProject(Project project) {
        this.projects.add(project);
        project.updateProject(project.getName(), project.getDescription());
    }
}