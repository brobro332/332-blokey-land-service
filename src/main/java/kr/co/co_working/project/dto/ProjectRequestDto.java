package kr.co.co_working.project.dto;

import kr.co.co_working.task.repository.entity.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
        private List<Task> tasks;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;
        public READ() { }
    }
}
