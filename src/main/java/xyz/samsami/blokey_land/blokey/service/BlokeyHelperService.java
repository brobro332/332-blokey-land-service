package xyz.samsami.blokey_land.blokey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqUpdateDto;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class BlokeyHelperService {
    private final WebClient authenticationWebClient;

    UUID createBlokeyOnAuthenticationServer(BlokeyReqCreateDto dto) {
        return authenticationWebClient.post()
            .uri("/blokeys")
            .bodyValue(dto)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(String.class).map(
                    body -> new CommonException(ExceptionType.BAD_REQUEST, body)
                )
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> response.bodyToMono(String.class).map(
                    body -> new CommonException(ExceptionType.EXTERNAL_API_ERROR, body)
                )
            )
            .bodyToMono(UUID.class)
            .block();
    }

    void updatePasswordOnAuthenticationServer(UUID blokeyId, BlokeyReqUpdateDto dto) {
        authenticationWebClient.patch()
            .uri("/blokeys/{blokeyId}/password", blokeyId)
            .bodyValue(Map.of("password", dto.getPassword()))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(String.class).map(
                    body -> new CommonException(ExceptionType.BAD_REQUEST, body)
                )
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> response.bodyToMono(String.class).map(
                    body -> new CommonException(ExceptionType.EXTERNAL_API_ERROR, body)
                )
            )
            .toBodilessEntity()
            .block();
    }

    void deleteBlokeyOnAuthenticationServer(UUID blokeyId) {
        authenticationWebClient.delete()
            .uri("/blokeys/{blokeyId}", blokeyId)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response.bodyToMono(String.class).map(
                    body -> new CommonException(ExceptionType.BAD_REQUEST, body)
                )
            )
            .onStatus(
                HttpStatusCode::is5xxServerError,
                response -> response.bodyToMono(String.class).map(
                    body -> new CommonException(ExceptionType.EXTERNAL_API_ERROR, body)
                )
            )
            .toBodilessEntity()
            .block();
    }
}