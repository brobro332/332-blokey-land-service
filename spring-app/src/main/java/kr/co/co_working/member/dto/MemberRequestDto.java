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

    @NoArgsConstructor
    @Getter
    @Setter
    public static class READ {
        private String email;
        private String name;
        private String teamId;

        public READ(String email, String name) {
            this.email = email;
            this.name = name;
        }

        public READ(String email, String name, String teamId) {
            this.email = email;
            this.name = name;
            this.teamId = teamId;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UPDATE {
        private String email;
        private String name;
        private String description;

        @Builder
        public UPDATE(String email, String name, String description) {
            this.email = email;
            this.name = name;
            this.description = description;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class DELETE {
        private String email;
    }
}