package xyz.samsami.blokey_land.blokey.domain;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public record BlokeySnapshot(
        String nickname,
        String bio,
        Set<Long> skills,
        Set<Long> disciplines
) {
    public static BlokeySnapshot from(Blokey blokey) {
        Set<Long> skillIds = blokey.getSkills().stream()
            .map(bs -> bs.getSkill().getId())
            .collect(Collectors.toSet());

        Set<Long> disciplineIds = blokey.getDisciplines().stream()
            .map(bd -> bd.getDiscipline().getId())
            .collect(Collectors.toSet());

        return new BlokeySnapshot(
            blokey.getNickname(),
            blokey.getBio(),
            skillIds,
            disciplineIds
        );
    }

    public boolean isEmbeddingFieldChanged(BlokeySnapshot snapshot) {
        return !skills.equals(snapshot.skills) || !disciplines.equals(snapshot.disciplines);
    }

    public boolean isIndexingOnlyFieldChanged(BlokeySnapshot snapshot) {
        return !Objects.equals(nickname, snapshot.nickname) || !Objects.equals(bio, snapshot.bio);
    }
}