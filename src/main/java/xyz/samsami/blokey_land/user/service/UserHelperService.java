package xyz.samsami.blokey_land.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.dto.UserReqCreateDto;
import xyz.samsami.blokey_land.user.dto.UserReqUpdateDto;
import xyz.samsami.blokey_land.user.repository.UserRepository;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserHelperService {
    private final UserRepository repository;
    private final WebClient authenticationWebClient;

    public UUID createUserOnAuthenticationServer(UserReqCreateDto dto) {
        return authenticationWebClient.post()
            .uri("/users")
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

    public void updatePasswordOnAuthenticationServer(UUID userId, UserReqUpdateDto dto) {
        authenticationWebClient.patch()
            .uri("/users/{userId}/password", userId)
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

    public void deleteUserOnAuthenticationServer(UUID userId) {
        authenticationWebClient.delete()
            .uri("/users/{userId}", userId)
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