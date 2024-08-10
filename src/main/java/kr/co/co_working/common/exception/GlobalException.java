package kr.co.co_working.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private ExceptionType exception;

    public GlobalException(ExceptionType e) {
        super(e.getMessage());
        this.exception = e;
    }
}
