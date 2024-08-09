package kr.co.co_working.task.dto;

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
    }

    @Getter
    @Setter
    public static class UPDATE {
        private Long id;
        private Long projectId;
        private String name;
        private String type;
        private String description;
    }

    @Getter
    @Setter
    public static class DELETE {
        private Long projectId;

        public DELETE() { }
    }
}
