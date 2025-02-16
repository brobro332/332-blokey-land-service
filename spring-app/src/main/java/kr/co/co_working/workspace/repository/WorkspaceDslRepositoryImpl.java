package kr.co.co_working.workspace.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.member.dto.MemberResponseDto;
import kr.co.co_working.member.dto.QMemberResponseDto;
import kr.co.co_working.workspace.dto.QWorkspaceResponseDto;
import kr.co.co_working.workspace.dto.WorkspaceRequestDto;
import kr.co.co_working.workspace.dto.WorkspaceResponseDto;
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
public class WorkspaceDslRepositoryImpl implements WorkspaceDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<WorkspaceResponseDto> readWorkspaceList(WorkspaceRequestDto.READ dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return factory
            .select(
                new QWorkspaceResponseDto(
                    workspace.id,
                    workspace.name,
                    workspace.description,
                    workspace.createdAt,
                    workspace.modifiedAt,
                    member.email,
                    member.name
                )
            )
            .from(workspace)
            .join(memberWorkspace).on(workspace.id.eq(memberWorkspace.workspace.id))
            .join(member).on(member.email.eq(memberWorkspace.member.email))
            .where(emailEq(email))
            .fetch();
    }

    public List<WorkspaceResponseDto> readWorkspaceListNotJoined(WorkspaceRequestDto.READ dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return factory
            .select(
                new QWorkspaceResponseDto(
                    workspace.id,
                    workspace.name,
                    workspace.description,
                    workspace.leader,
                    workspace.createdAt,
                    workspace.modifiedAt,
                    invitation.id,
                    invitation.status.stringValue()
                )
            ).from(workspace)
            .leftJoin(invitation).on(invitation.workspace.eq(workspace))
            .where(
                workspace.id.notIn(
                    JPAExpressions
                        .select(memberWorkspace.workspace.id)
                        .from(memberWorkspace)
                        .where(memberWorkspace.member.email.eq(email))
                ),
                nameContains(dto.getName()),
                invitation.member.email.eq(email)
            )
            .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null && !emailCond.trim().isEmpty() ? member.email.eq(emailCond) : null;
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null && !nameCond.trim().isEmpty() ? workspace.name.contains(nameCond) : null;
    }
}