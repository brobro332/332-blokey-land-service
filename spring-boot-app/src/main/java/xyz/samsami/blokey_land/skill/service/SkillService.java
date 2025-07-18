package xyz.samsami.blokey_land.skill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import xyz.samsami.blokey_land.skill.dto.GithubTopicItemDto;
import xyz.samsami.blokey_land.skill.dto.GithubTopicSearchRespDto;
import xyz.samsami.blokey_land.skill.repository.SkillRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {
    private final WebClient webClient;
    private final SkillRepository repository;

    public void saveTopicsAtoZ() {
        Flux.fromIterable("abcdefghijklmnopqrstuvwxyz".chars()
                .mapToObj(c -> String.valueOf((char) c))
                .toList())
            .flatMap(this::searchTopic)
            .distinct()
            .doOnNext(this::saveTopicUpsert)
            .blockLast();
    }

    private Flux<GithubTopicItemDto> searchTopic(String queryParam) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/search/topics")
                .queryParam("q", queryParam)
                .build())
            .retrieve()
            .bodyToMono(GithubTopicSearchRespDto.class)
            .flatMapMany(resp -> Flux.fromIterable(resp.items()));
    }

    @Transactional
    public void saveTopicUpsert(GithubTopicItemDto topic) {
        String displayName = topic.displayName() != null ? topic.displayName() : topic.name();
        repository.insertIgnoreConflict(topic.name(), displayName);
    }
}
