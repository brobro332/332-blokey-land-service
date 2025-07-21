package xyz.samsami.blokey_land.discipline.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineRespDto {
    private Long id;
    private String name;
}
