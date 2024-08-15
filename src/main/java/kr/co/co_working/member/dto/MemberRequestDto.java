package kr.co.co_working.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MemberRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private Long teamId;
        private String email;
        private String password;
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class READ {
        private String email;

        public READ() { }

        public READ(String email) {
            this.email = email;
        }
    }

    @Getter
    @Setter
    public static class UPDATE {
        private String name;
        private String description;
        private Long teamId;

        @Builder
        public UPDATE(String name, String description, Long teamId) {
            this.name = name;
            this.description = description;
            this.teamId = teamId;
        }
    }
}
