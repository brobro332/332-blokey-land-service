package kr.co.co_working.common.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalExceptionEntity {
    private String code;
    private String message;

    @Builder
    public GlobalExceptionEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
