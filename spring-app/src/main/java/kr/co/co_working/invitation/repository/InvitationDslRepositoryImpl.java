package kr.co.co_working.invitation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.invitation.RequesterType;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.dto.InvitationResponseDto;
import kr.co.co_working.invitation.dto.QInvitationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.invitation.QInvitation.invitation;
import static kr.co.co_working.member.QMember.member;
import static kr.co.co_working.workspace.QWorkspace.workspace;

@Repository
@RequiredArgsConstructor
public class InvitationDslRepositoryImpl implements InvitationDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<InvitationResponseDto> readInvitationList(InvitationRequestDto.READ dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        String menu = dto.getMenu();

        List<InvitationResponseDto> query;
        if ("MY_PAGE".equals(menu)) {
            query = factory
                .select(
                    new QInvitationResponseDto(
                        new CaseBuilder()
                            .when(invitation.requesterType.eq(RequesterType.valueOf("WORKSPACE"))).then("발신")
                            .when(invitation.requesterType.eq(RequesterType.valueOf("MEMBER"))).then("수신")
                            .otherwise(""),
                        workspace.id,
                        workspace.name,
                        workspace.description,
                        invitation.id,
                        invitation.status.stringValue(),
                        invitation.createdAt,
                        invitation.modifiedAt
                        )
                    )
                    .from(invitation)
                    .leftJoin(workspace)
                    .on(invitation.workspace.id.eq(workspace.id))
                    .where(
                        requesterTypeEq(dto.getDivision()),
                        emailEq(email),
                        workspaceNameContains(dto.getWorkspaceName())
                    )
                    .fetch();
        } else {
            query = factory
                        .select(
                            new QInvitationResponseDto(
                                new CaseBuilder()
                                    .when(invitation.requesterType.eq(RequesterType.valueOf("WORKSPACE"))).then("발신")
                                    .when(invitation.requesterType.eq(RequesterType.valueOf("MEMBER"))).then("수신")
                                    .otherwise(""),
                                member.email,
                                member.name,
                                member.description,
                                invitation.id,
                                invitation.status.stringValue(),
                                invitation.createdAt,
                                invitation.modifiedAt
                            )
                        )
                        .from(invitation)
                        .leftJoin(member)
                        .on(invitation.member.email.eq(member.email))
                        .where(
                            nameContains(dto.getName()),
                            member.delFlag.eq("0"),
                            requesterTypeEq(dto.getDivision()),
                            emailContains(dto.getEmail()),
                            workspaceIdEq(dto.getWorkspaceId())
                        )
                        .fetch();
        }
        return query;
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null && !emailCond.trim().isEmpty() ? invitation.member.email.eq(emailCond) : null;
    }

    private BooleanExpression emailContains(String emailCond) {
        return emailCond != null && !emailCond.trim().isEmpty() ? member.email.contains(emailCond) : null;
    }

    private BooleanExpression workspaceIdEq(Long idCond) {
        return idCond != null ? invitation.workspace.id.eq(idCond) : null;
    }

    private BooleanExpression workspaceNameContains(String workspaceNameCond) {
        return workspaceNameCond != null && !workspaceNameCond.trim().isEmpty() ? invitation.workspace.name.contains(workspaceNameCond) : null;
    }

    private BooleanExpression requesterTypeEq(String requesterTypeCond) {
        return requesterTypeCond != null && !requesterTypeCond.trim().isEmpty() ? invitation.requesterType.stringValue().eq(requesterTypeCond) : null;
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null && !nameCond.trim().isEmpty() ? member.name.contains(nameCond) : null;
    }
}