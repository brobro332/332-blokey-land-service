package xyz.samsami.blokey_land.matching.indexing.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import xyz.samsami.blokey_land.project.domain.Project;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "project")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Boolean)
    private boolean isPrivate;

    @Field(type = FieldType.Keyword)
    private List<String> skills;

    @Field(type = FieldType.Keyword)
    private List<String> disciplines;

    @Field(type = FieldType.Date)
    private LocalDate estimatedStartDate;

    @Field(type = FieldType.Date)
    private LocalDate estimatedEndDate;

    @Field(type = FieldType.Dense_Vector, dims = 1536)
    private float[] embedding;

    public static ProjectDocument toDocument(Project project, float[] embedding) {
        return ProjectDocument.builder()
            .id(project.getId())
            .title(project.getTitle())
            .description(project.getDescription())
            .status(project.getStatus() != null ? project.getStatus().name() : null)
            .isPrivate(project.isPrivate())
            .skills(project.getSkills().stream()
                .map(s -> s.getSkill().getName())
                .toList())
            .disciplines(project.getDisciplines().stream()
                .map(d -> d.getDiscipline().getName())
                .toList())
            .estimatedStartDate(project.getEstimatedStartDate())
            .estimatedEndDate(project.getEstimatedEndDate())
            .embedding(embedding)
            .build();
    }
}