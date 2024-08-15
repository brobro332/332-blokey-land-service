package kr.co.co_working.member.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * kr.co.co_working.member.dto.QMemberResponseDto is a Querydsl Projection type for MemberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberResponseDto extends ConstructorExpression<MemberResponseDto> {

    private static final long serialVersionUID = 859089174L;

    public QMemberResponseDto(com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> name, com.querydsl.core.types.Expression<String> description, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(MemberResponseDto.class, new Class<?>[]{String.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, email, name, description, createAt, modifiedAt);
    }

}

