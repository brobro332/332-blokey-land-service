package kr.co.co_working.workspace.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.co_working.invitation.InvitationStatus;
import kr.co.co_working.invitation.RequesterType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkspaceResponseDto {
    private Long id;
    private String name;
    private String description;
    private String leader;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private String email;
    private String memberName;
    private Long invitationId;
    private InvitationStatus invitationStatus;
    private RequesterType requesterType;

    @QueryProjection
    public WorkspaceResponseDto(Long id, String name, String description, LocalDateTime createAt, LocalDateTime modifiedAt, String email, String memberName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.email = email;
        this.memberName = memberName;
    }

    @QueryProjection
    public WorkspaceResponseDto(Long id, String name, String description, String leader, LocalDateTime createAt, LocalDateTime modifiedAt, Long invitationId, InvitationStatus invitationStatus, RequesterType requesterType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.invitationId = invitationId;
        this.invitationStatus = invitationStatus;
        this.requesterType = requesterType;
    }
}