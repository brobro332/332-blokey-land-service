package kr.co.co_working.team.dto;

import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.repository.entity.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class TeamRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String description;
        private String email;
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

        public UPDATE () { }

        @Builder
        public UPDATE(String name, String description, List<TaskRequestDto.UPDATE> tasks) {
            this.name = name;
            this.description = description;
        }
    }
}
