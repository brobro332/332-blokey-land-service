package xyz.samsami.blokey_land.skill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillRespDto {
    private Long id;
    private String name;
}
