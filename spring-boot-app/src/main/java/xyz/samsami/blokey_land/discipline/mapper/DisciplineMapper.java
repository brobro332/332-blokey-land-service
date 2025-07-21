package xyz.samsami.blokey_land.discipline.mapper;

import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;

public class DisciplineMapper {
    public static DisciplineRespDto toRespDto(Discipline discipline) {
        return DisciplineRespDto.builder()
            .id(discipline.getId())
            .name(discipline.getName())
            .build();
    }
}
