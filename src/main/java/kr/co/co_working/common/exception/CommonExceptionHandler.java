package kr.co.co_working.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    @ExceptionHandler({CommonException.class})
    public ResponseEntity<CommonExceptionEntity> exceptionHandler(CommonException e) {
        return ResponseEntity
                .status(e.getException().getStatus())
                .body(CommonExceptionEntity.builder()
                        .code(e.getException().getCode())
                        .message(e.getException().getMessage())
                        .build());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<CommonExceptionEntity> exceptionHandler(RuntimeException e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("예외 발생 지점 : ")
                .append(stackTraceElements[0].getClassName()).append(".")
                .append(stackTraceElements[0].getMethodName()).append(" ")
                .append("(line ").append(stackTraceElements[0].getLineNumber()).append(")");

        log.error("=".repeat(sb.length() + 5));
        log.error(sb.toString());
        log.error("=".repeat(sb.length() + 5));

        return ResponseEntity
                .status(ExceptionType.BAD_REQUEST.getStatus())
                .body(CommonExceptionEntity.builder()
                        .code(ExceptionType.BAD_REQUEST.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<CommonExceptionEntity> exceptionHandler(NoSuchElementException e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("예외 발생 지점 : ")
                .append(stackTraceElements[0].getClassName()).append(".")
                .append(stackTraceElements[0].getMethodName()).append(" ")
                .append("(line ").append(stackTraceElements[0].getLineNumber()).append(")");

        log.error("=".repeat(sb.length() + 5));
        log.error(sb.toString());
        log.error("=".repeat(sb.length() + 5));

        return ResponseEntity
                .status(ExceptionType.BAD_REQUEST.getStatus())
                .body(CommonExceptionEntity.builder()
                        .code(ExceptionType.BAD_REQUEST.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<CommonExceptionEntity> exceptionHandler(AccessDeniedException e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("예외 발생 지점 : ")
                .append(stackTraceElements[0].getClassName()).append(".")
                .append(stackTraceElements[0].getMethodName()).append(" ")
                .append("(line ").append(stackTraceElements[0].getLineNumber()).append(")");

        log.error("=".repeat(sb.length() + 5));
        log.error(sb.toString());
        log.error("=".repeat(sb.length() + 5));

        return ResponseEntity
                .status(ExceptionType.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(CommonExceptionEntity.builder()
                        .code(ExceptionType.ACCESS_DENIED_EXCEPTION.getCode())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonExceptionEntity> exceptionHandler(Exception e) {
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("예외 발생 지점 : ")
                .append(stackTraceElements[0].getClassName()).append(".")
                .append(stackTraceElements[0].getMethodName()).append(" ")
                .append("(line ").append(stackTraceElements[0].getLineNumber()).append(")");

        log.error("=".repeat(sb.length() + 5));
        log.error(sb.toString());
        log.error("=".repeat(sb.length() + 5));

        return ResponseEntity
                .status(ExceptionType.INTERNAL_SERVER_ERROR.getStatus())
                .body(CommonExceptionEntity.builder()
                        .code(ExceptionType.INTERNAL_SERVER_ERROR.getCode())
                        .message(e.getMessage())
                        .build());
    }
}
