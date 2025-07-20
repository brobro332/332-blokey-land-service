package xyz.samsami.blokey_land.project.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.samsami.blokey_land.discipline.dto.DisciplineAttachable;
import xyz.samsami.blokey_land.discipline.service.helper.DisciplineAttachHelper;
import xyz.samsami.blokey_land.skill.dto.SkillAttachable;
import xyz.samsami.blokey_land.skill.service.helper.SkillAttachHelper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectAttachHelper {
    private final SkillAttachHelper skillAttachHelper;
    private final DisciplineAttachHelper disciplineAttachHelper;

    public <T extends SkillAttachable<T> & DisciplineAttachable<T>> List<T> attachAll(List<T> dtoList) {
        List<T> withSkills = skillAttachHelper.attachSkills(dtoList);
        return disciplineAttachHelper.attachDisciplines(withSkills);
    }
}