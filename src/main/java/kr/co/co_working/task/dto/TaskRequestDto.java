package kr.co.co_working.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class TaskRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private Long projectId;
        private String name;
        private String type;
        private String description;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;
        private String type;
        private String description;

        @Builder
        public READ(String name, String type, String description) {
            this.name = name;
            this.type = type;
            this.description = description;
        }
    }

    @Getter
    @Setter
    public static class UPDATE {
        private Long id;
        private Long projectId;
        private String name;
        private String type;
        private String description;

        @Builder
        public UPDATE(Long projectId, String name, String type, String description) {
            this.projectId = projectId;
            this.name = name;
            this.type = type;
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
