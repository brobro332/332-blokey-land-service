package xyz.samsami.blokey_land.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.discipline.dto.DisciplineAttachable;
import xyz.samsami.blokey_land.discipline.dto.DisciplineRespDto;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.skill.dto.SkillAttachable;
import xyz.samsami.blokey_land.skill.dto.SkillRespDto;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProjectOnlyRespDto implements
    SkillAttachable<ProjectOnlyRespDto>,
    DisciplineAttachable<ProjectOnlyRespDto>
{
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private ProjectStatusType status;
    private boolean isPrivate;
    private boolean isLeader;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    @Builder.Default
    private Set<SkillRespDto> skills = Set.of();

    @Builder.Default
    private Set<DisciplineRespDto> disciplines = Set.of();

    @JsonProperty("isLeader")
    public boolean getIsLeader() {
        return isLeader;
    }

    public ProjectOnlyRespDto(
        Long id,
        String title,
        String description,
        String imageUrl,
        ProjectStatusType status,
        boolean isPrivate,
        boolean isLeader,
        LocalDate estimatedStartDate,
        LocalDate estimatedEndDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.isPrivate = isPrivate;
        this.isLeader = isLeader;
        this.estimatedStartDate = estimatedStartDate;
        this.estimatedEndDate = estimatedEndDate;
        this.actualStartDate = actualStartDate;
        this.actualEndDate = actualEndDate;
    }

    @Override
    public ProjectOnlyRespDto toBuilderWithSkills(Set<SkillRespDto> skills) {
        return this.toBuilder().skills(skills).build();
    }

    @Override
    public ProjectOnlyRespDto toBuilderWithDisciplines(Set<DisciplineRespDto> disciplines) {
        return this.toBuilder().disciplines(disciplines).build();
    }
}