package kr.co.co_working.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonExceptionEntity {
    private String code;
    private String errorMessage;

    @Builder
    public CommonExceptionEntity(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
