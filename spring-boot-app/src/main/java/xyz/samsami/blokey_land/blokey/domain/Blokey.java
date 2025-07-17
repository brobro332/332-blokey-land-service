package xyz.samsami.blokey_land.blokey.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;
import xyz.samsami.blokey_land.position.domain.BlokeyPosition;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Blokey extends CommonTimestamp {
    @Id
    private UUID id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(length = 200)
    private String bio;

    @OneToMany(mappedBy = "blokey", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<BlokeySkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "blokey", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<BlokeyPosition> positions = new HashSet<>();

    public void addSkill(BlokeySkill blokeySkill) {
        skills.add(blokeySkill);
        blokeySkill.updateBlokey(this);
    }

    public void removeSkill(BlokeySkill blokeySkill) {
        skills.remove(blokeySkill);
        blokeySkill.updateBlokey(null);
    }

    public void addPosition(BlokeyPosition blokeyPosition) {
        positions.add(blokeyPosition);
        blokeyPosition.updateBlokey(this);
    }

    public void removePosition(BlokeyPosition blokeyPosition) {
        positions.remove(blokeyPosition);
        blokeyPosition.updateBlokey(null);
    }

    public void updateNickname(String nickname) { if (nickname != null) this.nickname = nickname; }
    public void updateBio(String bio) { if (bio != null) this.bio = bio; }
}