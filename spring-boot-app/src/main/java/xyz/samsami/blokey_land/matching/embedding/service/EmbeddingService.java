package xyz.samsami.blokey_land.matching.embedding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.matching.embedding.infra.OllamaEmbeddingClient;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmbeddingService {
    private final OllamaEmbeddingClient client;
    private final BlokeyRepository blokeyRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public List<Float> embedBlokey(UUID blokeyId) {
        Blokey blokey = blokeyRepository.findById(blokeyId)
            .orElseThrow(() -> new CommonException(ExceptionType.NOT_FOUND, null));

        String bio = blokey.getBio();

        Set<String> skills = blokey.getSkills()
            .stream()
            .map(bs -> bs.getSkill().getDisplayName())
            .collect(Collectors.toSet());

        Set<String> disciplines = blokey.getDisciplines()
            .stream()
            .map(bd -> bd.getDiscipline().getName())
            .collect(Collectors.toSet());

        String combined = Stream.of(skills, disciplines, List.of(bio))
            .flatMap(Collection::stream)
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));

        return client.embed(combined);
    }

    @Transactional
    public List<Float> embedProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new CommonException(ExceptionType.NOT_FOUND, null));

        String description = project.getDescription();

        Set<String> skills = project.getSkills()
            .stream()
            .map(ps -> ps.getSkill().getDisplayName())
            .collect(Collectors.toSet());

        Set<String> disciplines = project.getDisciplines()
            .stream()
            .map(pd -> pd.getDiscipline().getName())
            .collect(Collectors.toSet());

        String combined = Stream.of(skills, disciplines, List.of(description))
            .flatMap(Collection::stream)
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));

        return client.embed(combined);
    }
}