package kr.co.co_working.team.repository.entity;

import jakarta.persistence.*;
import kr.co.co_working.common.entity.CommonTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateTeam(String name, String description) {
        this.name = name;
        this.description = description;
    }
}