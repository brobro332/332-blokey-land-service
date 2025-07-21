package xyz.samsami.blokey_land.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.discipline.dto.DisciplineAttachable;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.skill.dto.SkillAttachable;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProjectWithTaskRespDto implements
    SkillAttachable<ProjectWithTaskRespDto>,
    DisciplineAttachable<ProjectWithTaskRespDto>
{
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private ProjectStatusType status;
    private boolean isPrivate;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private List<TaskRespDto> tasks;

    @Builder.Default
    private Set<SkillRespDto> skills = Set.of();

    @Builder.Default
    private Set<DisciplineRespDto> disciplines = Set.of();

    @Override
    public ProjectWithTaskRespDto toBuilderWithSkills(Set<SkillRespDto> skills) {
        return this.toBuilder().skills(skills).build();
    }

    @Override
    public ProjectWithTaskRespDto toBuilderWithDisciplines(Set<DisciplineRespDto> disciplines) {
        return this.toBuilder().disciplines(disciplines).build();
    }
}
