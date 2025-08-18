package xyz.samsami.blokey_land.matching.indexing.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import xyz.samsami.blokey_land.blokey.domain.Blokey;

import java.util.List;
import java.util.UUID;

@Document(indexName = "blokey")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlokeyDocument {
    @Id
    private UUID id;

    @Field(type = FieldType.Text)
    private String nickname;

    @Field(type = FieldType.Text)
    private String bio;

    @Field(type = FieldType.Keyword)
    private List<String> skills;

    @Field(type = FieldType.Keyword)
    private List<String> disciplines;

    @Field(type = FieldType.Dense_Vector, dims = 1536)
    private float[] embedding;

    public static BlokeyDocument toDocument(Blokey blokey, float[] embedding) {
        return BlokeyDocument.builder()
            .id(blokey.getId())
            .nickname(blokey.getNickname())
            .bio(blokey.getBio())
            .skills(blokey.getSkills().stream()
                .map(s -> s.getSkill().getName())
                .toList())
            .disciplines(blokey.getDisciplines().stream()
                .map(d -> d.getDiscipline().getName())
                .toList())
            .embedding(embedding)
            .build();
    }
}