package xyz.samsami.blokey_land.skill.mapper;

import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;

public class SkillMapper {
    public static SkillRespDto toRespDto(Skill skill) {
        return SkillRespDto.builder()
            .id(skill.getId())
            .name(skill.getDisplayName())
            .build();
    }
}
