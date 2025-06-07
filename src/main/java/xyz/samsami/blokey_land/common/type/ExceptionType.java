package xyz.samsami.blokey_land.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    VALIDATION_ERROR("BAD_REQUEST", "잘못된 요청입니다.", 400),
    NOT_FOUND("NOT_FOUND", "요청하신 리소스를 찾을 수 없습니다.", 404),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.", 500);

    private final String code;
    private final String message;
    private final int status;
}