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
        private String name;
        private String teamId;

        public READ(String name) {
            this.name = name;
        }

        public READ(String name, String teamId) {
            this.name = name;
            this.teamId = teamId;
        }
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