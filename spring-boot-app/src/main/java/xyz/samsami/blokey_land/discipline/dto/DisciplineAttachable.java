package xyz.samsami.blokey_land.discipline.dto;

import java.util.Set;

public interface DisciplineAttachable<T extends DisciplineAttachable<T>> {
    Long getId();
    T toBuilderWithDisciplines(Set<DisciplineRespDto> disciplines);
}