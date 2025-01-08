package kr.co.co_working.project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class ProjectRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String description;
        private Long teamId;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;
        private Long teamId;

        public READ(String name, Long teamId) {
            this.name = name;
            this.teamId = teamId;
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