package kr.co.co_working.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonExceptionEntity {
    private String code;
    private String message;

    @Builder
    public CommonExceptionEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
