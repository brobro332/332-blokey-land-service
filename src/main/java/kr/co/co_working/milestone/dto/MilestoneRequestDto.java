package kr.co.co_working.milestone.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MilestoneRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private Long projectId;
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;
        private String description;

        @Builder
        public READ(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    @Getter
    @Setter
    public static class UPDATE {
        private Long id;
        private Long projectId;
        private String name;
        private String description;

        @Builder
        public UPDATE(Long projectId, String name, String description) {
            this.projectId = projectId;
            this.name = name;
            this.description = description;
        }
    }

    @Getter
    @Setter
    public static class DELETE {
        private Long projectId;

        public DELETE() { }
    }
}
