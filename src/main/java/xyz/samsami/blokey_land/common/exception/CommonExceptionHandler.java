package xyz.samsami.blokey_land.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.samsami.blokey_land.common.type.ExceptionType;

import java.time.OffsetDateTime;
import java.time.ZoneId;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<CommonExceptionEntity> handleCommonException(CommonException e) {
        log.error("🔺 공통 예외 발생", e);

        ExceptionType type = e.getException();
        CommonExceptionEntity entity = CommonExceptionEntity.builder()
            .code(type.getCode())
            .message(e.getMessage() != null ? e.getMessage() : type.getMessage())
            .timestamp(OffsetDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();

        return ResponseEntity.status(type.getStatus())
                .body(entity);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonExceptionEntity> handleRuntimeException(RuntimeException e) {
        log.error("🔺 런타임 예외 발생", e);

        CommonExceptionEntity entity = CommonExceptionEntity.builder()
            .code(ExceptionType.INTERNAL_SERVER_ERROR.getCode())
            .message(ExceptionType.INTERNAL_SERVER_ERROR.getMessage())
            .timestamp(OffsetDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entity);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonExceptionEntity> handleException(Exception e) {
        log.error("🔺 예기치 않은 예외 발생", e);

        CommonExceptionEntity entity = CommonExceptionEntity.builder()
            .code(ExceptionType.INTERNAL_SERVER_ERROR.getCode())
            .message(ExceptionType.INTERNAL_SERVER_ERROR.getMessage())
            .timestamp(OffsetDateTime.now(ZoneId.of("Asia/Seoul")))
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entity);
    }
}
