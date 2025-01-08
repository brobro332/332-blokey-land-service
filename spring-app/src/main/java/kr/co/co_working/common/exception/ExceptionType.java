package kr.co.co_working.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionType {
    /* 400 */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "E001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E002"),

    /* 500 */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E003");

    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionType(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }
}
