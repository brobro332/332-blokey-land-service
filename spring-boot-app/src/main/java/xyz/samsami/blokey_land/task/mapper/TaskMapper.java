package xyz.samsami.blokey_land.task.mapper;

import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;

public class TaskMapper {
    public static Task toEntity(TaskReqCreateDto dto, Project project) {
        return Task.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .assignee(dto.getAssignee())
            .progress(dto.getProgress())
            .status(dto.getStatus())
            .priority(dto.getPriority())
            .project(project)
            .estimatedStartDate(dto.getEstimatedStartDate())
            .estimatedEndDate(dto.getEstimatedEndDate())
            .actualStartDate(dto.getActualStartDate())
            .actualEndDate(dto.getActualEndDate())
            .build();
    }

    public static TaskRespDto toRespDto(Task task) {
        return TaskRespDto.builder()
            .id(task.getId())
            .projectId(task.getProject().getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .assignee(task.getAssignee())
            .progress(task.getProgress())
            .status(task.getStatus())
            .priority(task.getPriority())
            .estimatedStartDate(task.getEstimatedStartDate())
            .estimatedEndDate(task.getEstimatedEndDate())
            .actualStartDate(task.getActualStartDate())
            .actualEndDate(task.getActualEndDate())
            .build();
    }
}