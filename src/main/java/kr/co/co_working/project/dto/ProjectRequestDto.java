package kr.co.co_working.project.dto;

import lombok.Getter;
import lombok.Setter;

public class ProjectRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class UPDATE {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;
        private String description;
    }
}
