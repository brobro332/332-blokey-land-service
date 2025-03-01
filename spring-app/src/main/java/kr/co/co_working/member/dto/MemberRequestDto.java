package kr.co.co_working.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MemberRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String email;
        private String password;
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class READ {
        private String email;
        private String name;
        private Long workspaceId;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UPDATE {
        private String name;
        private String description;

        @Builder
        public UPDATE(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class PASSWORD {
        private String password;
    }
}