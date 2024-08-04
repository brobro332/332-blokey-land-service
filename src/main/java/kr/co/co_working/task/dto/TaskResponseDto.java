package kr.co.co_working.task.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskResponseDto {
        private Long id;
        private String name;
        private String type;
        private String description;
        private LocalDateTime createAt;
        private LocalDateTime modifiedAt;

        @QueryProjection
        public TaskResponseDto(Long id, String name, String type, String description, LocalDateTime createAt, LocalDateTime modifiedAt) {
                this.id = id;
                this.name = name;
                this.type = type;
                this.description = description;
                this.createAt = createAt;
                this.modifiedAt = modifiedAt;
        }
}
