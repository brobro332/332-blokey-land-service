package kr.co.co_working.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({GlobalException.class})
    public ResponseEntity<GlobalExceptionEntity> exceptionHandler(GlobalException e) {
        return ResponseEntity
                .status(e.getException().getStatus())
                .body(GlobalExceptionEntity.builder()
                        .code(e.getException().getCode())
                        .message(e.getException().getMessage())
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<GlobalExceptionEntity> exceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionType.BAD_REQUEST.getStatus())
                .body(GlobalExceptionEntity.builder()
                        .code(ExceptionType.BAD_REQUEST.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<GlobalExceptionEntity> exceptionHandler(AccessDeniedException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionType.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(GlobalExceptionEntity.builder()
                        .code(ExceptionType.ACCESS_DENIED_EXCEPTION.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<GlobalExceptionEntity> exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ExceptionType.INTERNAL_SERVER_ERROR.getStatus())
                .body(GlobalExceptionEntity.builder()
                        .code(ExceptionType.INTERNAL_SERVER_ERROR.getCode())
                        .message(e.getMessage())
                        .build());
    }
}
