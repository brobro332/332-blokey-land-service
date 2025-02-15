package kr.co.co_working.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.member.dto.MemberRequestDto;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.dto.QMemberResponseDto;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.invitation.QInvitation.invitation;
import static kr.co.co_working.member.QMember.member;
import static kr.co.co_working.memberWorkspace.QMemberWorkspace.memberWorkspace;
import static kr.co.co_working.workspace.QWorkspace.workspace;

@Repository
@RequiredArgsConstructor
public class MemberDslRepositoryImpl implements MemberDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<MemberResponseDto> readMembers(MemberRequestDto.READ dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return factory
            .select(
                new QMemberResponseDto(
                    member.email,
                    member.name,
                    member.description,
                    member.createdAt,
                    member.modifiedAt
                )
            )
            .from(member)
            .where(emailEq(email))
            .where(nameContains(dto.getName()))
            .where(member.delFlag.eq("0"))
            .fetch();
    }

    @Override
    public List<MemberResponseDto> readMemberListInWorkspace(WorkspaceRequestDto.READ dto) {
        return factory
            .select(
                new QMemberResponseDto(
                    member.email,
                    member.name,
                    member.description,
                    member.createdAt,
                    member.modifiedAt,
                    workspace.leader
                )
            )
            .from(workspace)
            .join(memberWorkspace).on(workspace.id.eq(memberWorkspace.workspace.id))
            .join(member).on(member.email.eq(memberWorkspace.member.email))
            .where(WorkspaceIdEq(dto.getId()).and(member.delFlag.eq("0")))
            .fetch();
    }

    @Override
    public List<MemberResponseDto> readMemberListNotInWorkspace(WorkspaceRequestDto.READ dto) {
        return factory
            .select(
                new QMemberResponseDto(
                    member.email,
                    member.name,
                    member.description,
                    member.createdAt,
                    member.modifiedAt,
                    invitation.id,
                    invitation.status
                )
            ).from(member)
            .leftJoin(invitation).on(invitation.member.eq(member))
            .where(
                member.email.notIn(
                    JPAExpressions
                        .select(memberWorkspace.member.email)
                        .from(memberWorkspace)
                        .where(memberWorkspace.workspace.id.eq(dto.getId()))
                )
            )
            .where(
                emailContains(dto.getEmail()),
                nameContains(dto.getName())
            )
            .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null && !emailCond.trim().isEmpty() ? member.email.eq(emailCond) : null;
    }

    private BooleanExpression emailContains(String emailCond) {
        return emailCond != null && !emailCond.trim().isEmpty() ? member.email.contains(emailCond) : null;
    }

    private BooleanExpression WorkspaceIdEq(Long idCond) {
        return idCond != null ? workspace.id.eq(idCond) : null;
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null && !nameCond.trim().isEmpty() ? member.name.contains(nameCond) : null;
    }
}