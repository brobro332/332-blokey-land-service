package xyz.samsami.blokey_land.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends CommonTimestamp {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(length = 1000)
    private String bio;

    /* TODO: 스킬 도메인 개발
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();
    */

    /* TODO: 포지션 도메인 개발
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Position> positions = new ArrayList<>();
    */

    public void updateNickname(String nickname) {
        if (nickname != null) this.nickname = nickname;
    }

    public void updateBio(String bio) {
        if (bio != null) this.bio = bio;
    }

    /* TODO: 스킬 필드 업데이트 메서드
    public void updateSkills(List<Skill> skills) {
        this.skills.clear();
        skills.forEach(skill -> this.skills.add(skill));
    }
    */

    /* TODO: 포지션 필드 업데이트 메서드
    public void updatePositions(List<Position> positions) {
        this.positions.clear();
        positions.forEach(position -> this.positions.add(position));
    }
    */
}