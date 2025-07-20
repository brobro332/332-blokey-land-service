package xyz.samsami.blokey_land.discipline.service.helper;

import org.springframework.stereotype.Component;
import xyz.samsami.blokey_land.discipline.dto.DisciplineAttachable;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;
import xyz.samsami.blokey_land.discipline.service.ProjectDisciplineService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DisciplineAttachHelper {
    private final ProjectDisciplineService projectDisciplineService;

    public DisciplineAttachHelper(ProjectDisciplineService projectDisciplineService) {
        this.projectDisciplineService = projectDisciplineService;
    }

    public <T extends DisciplineAttachable<T>> List<T> attachDisciplines(List<T> dtoList) {
        Set<Long> projectIds = dtoList.stream().map(DisciplineAttachable::getId).collect(Collectors.toSet());
        Map<Long, Set<DisciplineRespDto>> disciplineMap = projectDisciplineService.findWithDisciplineByProjectIds(projectIds).stream()
            .collect(Collectors.groupingBy(
                ps -> ps.getProject().getId(),
                Collectors.mapping(
                    ps -> new DisciplineRespDto(ps.getSkill().getId(), ps.getSkill().getName()),
                    Collectors.toSet()
                ))
            );

        return dtoList.stream()
            .map(dto -> dto.toBuilderWithDisciplines(disciplineMap.getOrDefault(dto.getId(), Set.of())))
            .collect(Collectors.toList());
    }
}
