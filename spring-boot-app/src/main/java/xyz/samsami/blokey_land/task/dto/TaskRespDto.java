package xyz.samsami.blokey_land.task.dto;

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
public class TaskRespDto {
    private Long id;
    private Long projectId;
    private String title;
    private String description;
    private UUID assignee;
    private Integer progress;
    private TaskStatusType status;
    private PriorityType priority;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
}