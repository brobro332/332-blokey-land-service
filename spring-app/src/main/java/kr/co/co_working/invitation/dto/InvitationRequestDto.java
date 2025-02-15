package kr.co.co_working.invitation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    public static class READ {
        private String division;
        private String email;
        private String name;
        private LocalDateTime createdAtFrom;
        private LocalDateTime createdAtTo;
        private Long workspaceId;
        private String workspaceName;
        private String menu;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DELETE {
        private Long id;
    }
}