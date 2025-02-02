package kr.co.co_working.invitation.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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

import static kr.co.co_working.member.QMember.member;
import static kr.co.co_working.memberWorkspace.QMemberWorkspace.memberWorkspace;
import static kr.co.co_working.workspace.QWorkspace.workspace;
import static kr.co.co_working.invitation.QInvitation.invitation;

@Repository
@RequiredArgsConstructor
public class InvitationDslRepositoryImpl implements InvitationDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<InvitationResponseDto> readInvitationList(InvitationRequestDto.READ dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return factory
            .select(
                new QInvitationResponseDto(
                    new CaseBuilder()
                            .when(invitation.requesterType.eq(RequesterType.valueOf("WORKSPACE"))).then("발신")
                            .otherwise("수신"),
                    member.email,
                    member.name,
                    member.description,
                    invitation.createdAt,
                    invitation.modifiedAt
                )
            )
            .from(invitation)
            .join(memberWorkspace).on(invitation.member.email.eq(memberWorkspace.member.email))
            .join(member).on(memberWorkspace.member.email.eq(memberWorkspace.member.email))
            .where(
                emailEq(email)
                    .and(nameContains(dto.getName()))
                    .and(WorkspaceIdEq(dto.getTeamId()))
                    .and(member.delFlag.eq("0"))
                    .and(invitation.requesterType.stringValue().eq(dto.getDivision()))
            )
            .fetch();
    }

    private BooleanExpression emailEq(String emailCond) {
        return emailCond != null ? member.email.eq(emailCond) : null;
    }

    private BooleanExpression WorkspaceIdEq(Long idCond) {
        return idCond != null ? workspace.id.eq(idCond) : null;
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? member.name.contains(nameCond) : null;
    }
}