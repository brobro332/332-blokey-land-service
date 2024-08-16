package kr.co.co_working.team.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * kr.co.co_working.team.dto.QTeamResponseDto is a Querydsl Projection type for TeamResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QTeamResponseDto extends ConstructorExpression<TeamResponseDto> {

    private static final long serialVersionUID = -226447498L;

    public QTeamResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(TeamResponseDto.class, new Class<?>[]{long.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, name, description, createAt, modifiedAt);
    }

}

