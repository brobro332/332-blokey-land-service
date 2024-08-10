package kr.co.co_working.task.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * kr.co.co_working.task.dto.QTaskResponseDto is a Querydsl Projection type for TaskResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QTaskResponseDto extends ConstructorExpression<TaskResponseDto> {

    private static final long serialVersionUID = 1414234742L;

    public QTaskResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> type, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(TaskResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, name, type, description, createAt, modifiedAt);
    }

}

