package xyz.samsami.blokey_land.skill.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import xyz.samsami.blokey_land.skill.dto.GithubTopicItemDto;
import xyz.samsami.blokey_land.skill.dto.GithubTopicSearchRespDto;
import xyz.samsami.blokey_land.skill.repository.SkillRepository;

import java.util.List;
import java.util.function.Function;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {
    @InjectMocks private SkillService service;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS) private WebClient webClient;
    @Mock private SkillRepository repository;

    @Test
    @DisplayName("유효한 파라미터가 주어지면 저장 메서드가 호출되어야 한다.")
    void givenValidParameter_whenSaveTopicsAtoZ_thenCallInsertIgnoreConflict() {
        // given
        GithubTopicItemDto item = new GithubTopicItemDto("java", "Java");
        GithubTopicSearchRespDto respDto = new GithubTopicSearchRespDto(List.of(item));

        when(
            webClient.get()
                .uri(any(Function.class))
                .retrieve()
                .bodyToMono(GithubTopicSearchRespDto.class)
        ).thenReturn(Mono.just(respDto));

        doNothing().when(repository).insertIgnoreConflict(anyString(), anyString());

        // when
        service.saveTopicsAtoZ();

        // then
        verify(repository, atLeastOnce()).insertIgnoreConflict(eq("java"), eq("Java"));
    }
}