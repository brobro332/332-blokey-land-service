package kr.co.co_working.invitation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class InvitationRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private Long workspaceId;
        private String memberId;
        private String requesterType;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DELETE {
        private Long id;
    }
}