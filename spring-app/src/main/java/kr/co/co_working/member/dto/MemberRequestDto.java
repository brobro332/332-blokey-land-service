package kr.co.co_working.member.dto;

import lombok.Builder;
import lombok.Getter;
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

        public READ() { }

        public READ(String email, String name) {
            this.email = email;
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class UPDATE {
        private String email;
        private String name;
        private String description;

        public UPDATE () { }

        @Builder
        public UPDATE(String email, String name, String description) {
            this.email = email;
            this.name = name;
            this.description = description;
        }
    }

    @Getter
    @Setter
    public static class DELETE {
        private String email;

        public DELETE() { }
    }
}