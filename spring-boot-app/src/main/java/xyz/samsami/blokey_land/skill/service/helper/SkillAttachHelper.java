package xyz.samsami.blokey_land.skill.service.helper;

import org.springframework.stereotype.Component;
import xyz.samsami.blokey_land.skill.dto.SkillAttachable;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;
import xyz.samsami.blokey_land.skill.service.ProjectSkillService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SkillAttachHelper {
    private final ProjectSkillService projectSkillService;

    public SkillAttachHelper(ProjectSkillService projectSkillService) {
        this.projectSkillService = projectSkillService;
    }

    public <T extends SkillAttachable<T>> List<T> attachSkills(List<T> dtoList) {
        Set<Long> projectIds = dtoList.stream().map(SkillAttachable::getId).collect(Collectors.toSet());
        Map<Long, Set<SkillRespDto>> skillMap = projectSkillService.findWithSkillByProjectIds(projectIds).stream()
            .collect(Collectors.groupingBy(
                ps -> ps.getProject().getId(),
                Collectors.mapping(
                    ps -> new SkillRespDto(ps.getSkill().getId(), ps.getSkill().getName()),
                    Collectors.toSet()
                ))
            );

        return dtoList.stream()
            .map(dto -> dto.toBuilderWithSkills(skillMap.getOrDefault(dto.getId(), Set.of())))
            .collect(Collectors.toList());
    }
}