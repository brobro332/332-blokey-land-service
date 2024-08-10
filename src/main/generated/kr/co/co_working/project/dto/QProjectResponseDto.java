package kr.co.co_working.project.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * kr.co.co_working.project.dto.QProjectResponseDto is a Querydsl Projection type for ProjectResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QProjectResponseDto extends ConstructorExpression<ProjectResponseDto> {

    private static final long serialVersionUID = 1804447678L;

    public QProjectResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(ProjectResponseDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, name, description, createAt, modifiedAt);
    }

}

