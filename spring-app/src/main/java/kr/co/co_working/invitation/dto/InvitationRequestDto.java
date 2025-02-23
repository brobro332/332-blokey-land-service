package kr.co.co_working.invitation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class InvitationRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String memberEmail;
        private Long workspaceId;
        private String requesterType;
    }

    @Getter
    @Setter
    public static class READ {
        private String memberEmail;
        private String memberName;
        private Long workspaceId;
        private String workspaceName;
        private String division;
        private String menu;
        private LocalDateTime createdAtFrom;
        private LocalDateTime createdAtTo;
    }

    @Getter
    @Setter
    public static class UPDATE {
        private String memberEmail;
        private Long workspaceId;
        private Long id;
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class DELETE {
        private Long id;
    }
}