package kr.co.co_working.common.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.co_working.common.dto.AuthenticationRequestDto;
import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.common.jwt.JwtProvider;
import kr.co.co_working.common.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationApiController {
    private final AuthenticationService service;
    private final JwtProvider provider;

    @PostMapping("/api/v1/authentication")
    public ResponseDto<?> login(HttpServletResponse response, @RequestBody AuthenticationRequestDto.LOGIN dto) throws NoSuchElementException, Exception {
        // 1. 사용자 확인
        boolean isValid = service.isValidMember(dto);

        // 2. 액세스 토큰, 리프레시 토큰 HTTP Only 쿠키에 담아 반환
        Map<String, String> tokens = new HashMap<>();
        if (isValid) {
            tokens = provider.createTokens(dto.getEmail());

            String accessTokenCookie = "Authorization=" + tokens.get("accessToken") + "; HttpOnly; Path=/; SameSite=Strict";
            String refreshTokenCookie = "RefreshToken=" + tokens.get("refreshToken") + "; HttpOnly; Path=/; SameSite=Strict";

            response.addHeader("Set-Cookie", accessTokenCookie);
            response.addHeader("Set-Cookie", refreshTokenCookie);
        }

        return ResponseDto.ofSuccess("로그인에 성공했습니다.");
    }
}
