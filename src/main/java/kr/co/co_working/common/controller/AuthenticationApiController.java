package kr.co.co_working.common.controller;

import kr.co.co_working.common.dto.AuthenticationRequestDto;
import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.common.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class AuthenticationApiController {
    private final AuthenticationService service;

    @PostMapping("/api/v1/authentication")
    public ResponseDto<?> login(@RequestBody AuthenticationRequestDto.LOGIN dto) throws NoSuchElementException, Exception {
        // 1. 로그인 유효 여부 확인
        boolean isExists = service.isValidMember(dto);

        // 2. 액세스 토큰, 리프레시 토큰 반환
        Map<String, String> tokens = new HashMap<>();
        if (isExists) {
            // ...
        }

        return ResponseDto.ofSuccess("로그인에 성공했습니다.", tokens);
    }
}
