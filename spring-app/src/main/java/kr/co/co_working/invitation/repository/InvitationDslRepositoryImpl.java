package kr.co.co_working.invitation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.invitation.RequesterType;
import kr.co.co_working.invitation.dto.InvitationRequestDto;
import kr.co.co_working.invitation.dto.InvitationResponseDto;
import kr.co.co_working.invitation.dto.QInvitationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.invitation.QInvitation.invitation;
import static kr.co.co_working.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class InvitationDslRepositoryImpl implements InvitationDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<InvitationResponseDto> readInvitationList(InvitationRequestDto.READ dto) {
        String menu = dto.getMenu();
        BooleanBuilder builder = new BooleanBuilder();

        if ("MY_PAGE".equals(menu)) {
            builder
                .and(emailEq(dto.getEmail()))
                .and(workspaceNameContains(dto.getWorkspaceName()));
        } else if ("WORKSPACE".equals(menu)) {
            builder
                .and(emailContains(dto.getEmail()))
                .and(workspaceIdEq(dto.getWorkspaceId()));
        }

        return factory
            .select(
                new QInvitationResponseDto(
                    new CaseBuilder()
                            .when(invitation.requesterType.eq(RequesterType.valueOf("WORKSPACE"))).then("발신")
                            .when(invitation.requesterType.eq(RequesterType.valueOf("MEMBER"))).then("수신")
                            .otherwise(""),
                    member.email,
                    member.name,
                    member.description,
                    invitation.status.stringValue(),
                    invitation.createdAt,
                    invitation.modifiedAt
                )
            )
            .from(invitation)
            .leftJoin(member).on(invitation.member.email.eq(member.email))
            .where(
                nameContains(dto.getName()),
                member.delFlag.eq("0"),
                requesterTypeEq(dto.getDivision()),
                builder
            )
            .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null && !emailCond.trim().isEmpty() ? member.email.eq(emailCond) : null;
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