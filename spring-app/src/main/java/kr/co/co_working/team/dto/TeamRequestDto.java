package kr.co.co_working.team.dto;

import kr.co.co_working.task.dto.TaskRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class TeamRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String email;
        private String name;
        private String description;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class READ {
        private String email;
        private Long id;

        public READ(String email) {
            this.email = email;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UPDATE {
        private String email;
        private String name;
        private String description;

        @Builder
        public UPDATE(String name, String description, List<TaskRequestDto.UPDATE> tasks) {
            this.name = name;
            this.description = description;
        }
    }
}
