package kr.co.co_working.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.co.co_working.invitation.InvitationStatus;
import kr.co.co_working.invitation.RequesterType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponseDto {
        private String email;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String leader;
        private InvitationStatus status;
        private Long invitationId;
        private RequesterType requesterType;

        @QueryProjection
        public MemberResponseDto(String email, String name, String description, LocalDateTime createdAt, LocalDateTime modifiedAt) {
                this.email = email;
                this.name = name;
                this.description = description;
                this.createdAt = createdAt;
                this.modifiedAt = modifiedAt;
        }

        @QueryProjection
        public MemberResponseDto(String email, String name, String description, LocalDateTime createdAt, LocalDateTime modifiedAt, Long invitationId, InvitationStatus status, RequesterType requesterType) {
                this.email = email;
                this.name = name;
                this.description = description;
                this.createdAt = createdAt;
                this.modifiedAt = modifiedAt;
                this.invitationId = invitationId;
                this.status = status;
                this.requesterType = requesterType;
        }

        @QueryProjection
        public MemberResponseDto(String email, String name, String description, LocalDateTime createdAt, LocalDateTime modifiedAt, String leader) {
                this.email = email;
                this.name = name;
                this.description = description;
                this.createdAt = createdAt;
                this.modifiedAt = modifiedAt;
                this.leader = leader;
        }
}
