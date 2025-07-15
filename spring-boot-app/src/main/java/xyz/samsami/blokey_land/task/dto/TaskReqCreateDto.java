package xyz.samsami.blokey_land.task.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.task.type.PriorityType;
import xyz.samsami.blokey_land.task.type.TaskStatusType;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskReqCreateDto {
    @NotBlank
    @Size(min = 2, max = 100, message = "태스크 제목은 2자 이상 100자 이하로 입력해주세요.")
    private String title;

    @Min(0) @Max(100)
    private Integer progress;

    private String description;
    private Long projectId;
    private UUID assignee;
    private TaskStatusType status;
    private PriorityType priority;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
}