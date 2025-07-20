package xyz.samsami.blokey_land.blokey.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlokeyRespDto {
    private UUID id;
    private String nickname;
    private String bio;
    private boolean hasPendingOffer;

    @Builder.Default
    private Set<SkillRespDto> skills = new HashSet<>();
    @Builder.Default
    private Set<DisciplineRespDto> disciplines = new HashSet<>();

    @QueryProjection
    public BlokeyRespDto(UUID id, String nickname, String bio, boolean hasPendingOffer) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.hasPendingOffer = hasPendingOffer;
        this.skills = Set.of();
        this.disciplines = Set.of();
    }

    public void updateSkills(Set<SkillRespDto> skills) {
        if (skills != null) this.skills = new HashSet<>(skills);
    }

    public void updateDisciplines(Set<DisciplineRespDto> disciplines) {
        if (disciplines != null) this.disciplines = new HashSet<>(disciplines);
    }
}