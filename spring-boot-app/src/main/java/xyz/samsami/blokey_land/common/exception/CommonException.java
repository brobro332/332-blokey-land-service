package xyz.samsami.blokey_land.common.exception;

import lombok.Getter;
import xyz.samsami.blokey_land.common.type.ExceptionType;

@Getter
public class CommonException extends RuntimeException {
    private final ExceptionType exception;

    public CommonException(ExceptionType e, String message) {
        super(message);
        this.exception = e;
    }
}