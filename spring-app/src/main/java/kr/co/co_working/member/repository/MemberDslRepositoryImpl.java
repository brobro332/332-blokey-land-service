package kr.co.co_working.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.dto.QMemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberDslRepositoryImpl implements MemberDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT member_email, member_name, member_description, member_createdAt, member_modifiedAt
     * FROM tbl_member
     * WHERE member_email = ?
     * AND member_name LIKE ?
     *
     * @param dto
     * @return
     */
    @Override
    public List<MemberResponseDto> readMemberList(MemberRequestDto.READ dto) {
        return factory
                .select(new QMemberResponseDto(member.email, member.name, member.description, member.createdAt, member.modifiedAt))
                .from(member)
                .where(emailEq(dto.getEmail()))
                .where(nameContains(dto.getName()))
                .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null ? member.email.eq(emailCond) : null;
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? member.name.contains(nameCond) : null;
    }
}