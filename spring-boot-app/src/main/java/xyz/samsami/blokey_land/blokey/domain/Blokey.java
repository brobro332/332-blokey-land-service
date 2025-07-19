package xyz.samsami.blokey_land.blokey.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;
import xyz.samsami.blokey_land.discipline.domain.BlokeyDiscipline;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.RelationTarget;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Blokey extends CommonTimestamp implements RelationTarget {
    @Id
    private UUID id;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(length = 200)
    private String bio;

    @OneToMany(mappedBy = "blokey", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private Set<BlokeySkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "blokey", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private Set<BlokeyDiscipline> disciplines = new HashSet<>();

    public void addSkill(BlokeySkill blokeySkill) {
        skills.add(blokeySkill);
        blokeySkill.updateBlokey(this);
    }

    public void removeSkill(BlokeySkill blokeySkill) {
        skills.remove(blokeySkill);
        blokeySkill.updateBlokey(null);
    }

    public void addDiscipline(BlokeyDiscipline blokeyDiscipline) {
        disciplines.add(blokeyDiscipline);
        blokeyDiscipline.updateBlokey(this);
    }

    public void removeDiscipline(BlokeyDiscipline blokeyDiscipline) {
        disciplines.remove(blokeyDiscipline);
        blokeyDiscipline.updateBlokey(null);
    }

    public void updateNickname(String nickname) { if (nickname != null) this.nickname = nickname; }
    public void updateBio(String bio) { if (bio != null) this.bio = bio; }
}