package xyz.samsami.blokey_land.skill.dto;

import java.util.Set;

public interface SkillAttachable<T extends SkillAttachable<T>> {
    Long getId();
    T toBuilderWithSkills(Set<SkillRespDto> skills);
}