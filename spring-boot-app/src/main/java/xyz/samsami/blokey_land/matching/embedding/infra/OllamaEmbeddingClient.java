package xyz.samsami.blokey_land.matching.embedding.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.samsami.blokey_land.matching.embedding.dto.OllamaRespDto;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OllamaEmbeddingClient {
    private final WebClient webClient;

    public List<Float> embed(String text) {
        Map<String, String> request = Map.of(
            "model", "bge-m3",
            "input", text
        );

        var response = webClient.post()
            .uri("/api/embeddings")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaRespDto.class)
            .block();

        return response != null ? response.embedding() : List.of();
    }
}
