package kr.co.co_working.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private ExceptionType exception;

    public CommonException(ExceptionType e) {
        super(e.getMessage());
        this.exception = e;
    }
}
