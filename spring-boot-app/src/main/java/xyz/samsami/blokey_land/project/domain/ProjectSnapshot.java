package xyz.samsami.blokey_land.project.domain;

import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record ProjectSnapshot(
        String title,
        String description,
        String imageUrl,
        ProjectStatusType status,
        boolean isPrivate,
        LocalDate estimatedStartDate,
        LocalDate estimatedEndDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate,
        Set<Long> skills,
        Set<Long> disciplines
) {
    public static ProjectSnapshot from(Project project) {
        Set<Long> skillIds = project.getSkills().stream()
            .map(ps -> ps.getSkill().getId())
            .collect(Collectors.toSet());

        Set<Long> disciplineIds = project.getDisciplines().stream()
            .map(pd -> pd.getDiscipline().getId())
            .collect(Collectors.toSet());

        return new ProjectSnapshot(
            project.getTitle(),
            project.getDescription(),
            project.getImageUrl(),
            project.getStatus(),
            project.isPrivate(),
            project.getEstimatedStartDate(),
            project.getEstimatedEndDate(),
            project.getActualStartDate(),
            project.getActualEndDate(),
            skillIds,
            disciplineIds
        );
    }

    public boolean isEmbeddingFieldChanged(ProjectSnapshot other) {
        return !skills.equals(other.skills)
            || !disciplines.equals(other.disciplines)
            || !Objects.equals(description, other.description);
    }

    public boolean isIndexingOnlyFieldChanged(ProjectSnapshot other) {
        return !Objects.equals(title, other.title)
            || !Objects.equals(imageUrl, other.imageUrl)
            || status != other.status
            || isPrivate != other.isPrivate
            || !Objects.equals(estimatedStartDate, other.estimatedStartDate)
            || !Objects.equals(estimatedEndDate, other.estimatedEndDate)
            || !Objects.equals(actualStartDate, other.actualStartDate)
            || !Objects.equals(actualEndDate, other.actualEndDate);
    }
}