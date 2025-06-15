package xyz.samsami.blokey_land.blokey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.vo.AccountVo;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class BlokeyHelperService {
    private final WebClient authenticationWebClient;

    CommonRespDto<AccountVo> createBlokeyOnAuthenticationServer(BlokeyReqCreateDto dto) {
        return authenticationWebClient.post()
            .uri("/api/accounts")
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
            .bodyToMono(new ParameterizedTypeReference<CommonRespDto<AccountVo>>() {})
            .block();
    }
}