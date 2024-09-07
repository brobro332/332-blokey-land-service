package kr.co.co_working.milestone.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class MilestoneRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private Long projectId;
        private String name;
        private String description;
        private LocalDateTime dueAt;
    }

    @Getter
    @Setter
    public static class READ {
        private Long projectId;
        private String name;
        private String description;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        @Builder
        public READ(Long projectId, String name, String description, LocalDateTime startAt, LocalDateTime endAt) {
            this.projectId = projectId;
            this.name = name;
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
        private String description;
        private LocalDateTime dueAt;

        @Builder
        public UPDATE(Long id, Long projectId, String name, String description, LocalDateTime dueAt) {
            this.id = id;
            this.projectId = projectId;
            this.name = name;
            this.description = description;
            this.dueAt = dueAt;
        }
    }

    @Getter
    @Setter
    public static class DELETE {
        private Long id;
        private Long projectId;

        public DELETE(Long id, Long projectId) {
            this.id = id;
            this.projectId = projectId;
        }
    }
}
