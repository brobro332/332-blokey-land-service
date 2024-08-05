package kr.co.co_working.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectResponseDto {
        private Long id;
        private String name;
        private String description;
        private LocalDateTime createAt;
        private LocalDateTime modifiedAt;
}
