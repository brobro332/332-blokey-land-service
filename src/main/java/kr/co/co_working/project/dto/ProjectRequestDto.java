package kr.co.co_working.project.dto;

import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.repository.entity.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProjectRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String description;
        private List<Task> tasks;
    }

    @Getter
    @Setter
    public static class READ {
        private String name;

        public READ() { }

        public READ(String name) {
            this.name = name;
        }
    }

    @Getter
    @Setter
    public static class UPDATE {
        private String name;
        private String description;
        private List<TaskRequestDto.UPDATE> tasks;

        @Builder
        public UPDATE(String name, String description, List<TaskRequestDto.UPDATE> tasks) {
            this.name = name;
            this.description = description;
            this.tasks = tasks;
        }
    }
}
