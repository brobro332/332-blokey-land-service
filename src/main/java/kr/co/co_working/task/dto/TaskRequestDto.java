package kr.co.co_working.task.dto;

import lombok.Getter;
import lombok.Setter;

public class TaskRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String type;
        private String description;
    }

    @Getter
    @Setter
    public static class UPDATE {
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
}
