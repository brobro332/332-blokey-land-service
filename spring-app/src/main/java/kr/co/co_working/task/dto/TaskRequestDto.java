package kr.co.co_working.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TaskRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private Long projectId;
        private String email;
        private String name;
        private String type;
        private String description;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;
        private String type;
        private String description;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        @Builder
        public READ(String name, String type, String description, LocalDateTime startAt, LocalDateTime endAt) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.startAt = startAt;
            this.endAt = endAt;
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
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        @Builder
        public UPDATE(Long projectId, String name, String type, String description, LocalDateTime startAt, LocalDateTime endAt) {
            this.projectId = projectId;
            this.name = name;
            this.type = type;
            this.description = description;
            this.startAt = startAt;
            this.endAt = endAt;
        }
    }

    @Getter
    @Setter
    public static class DELETE {
        private Long projectId;

        public DELETE() { }
    }
}
