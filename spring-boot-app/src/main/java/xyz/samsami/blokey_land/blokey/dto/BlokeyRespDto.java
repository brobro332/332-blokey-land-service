package xyz.samsami.blokey_land.blokey.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @QueryProjection
    public BlokeyRespDto(UUID id, String nickname, String bio, boolean hasOffer) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.hasPendingOffer = hasOffer;
        this.skills = new HashSet<>();
    }

    public void updateSkillRespDtoSet(Set<SkillRespDto> skills) {
        if (skills != null) this.skills = new HashSet<>(skills);
    }
}