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
            .build();
    }

    public static TaskRespDto toRespDto(Task task) {
        return TaskRespDto.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .assignee(task.getAssignee())
            .progress(task.getProgress())
            .status(task.getStatus())
            .priority(task.getPriority())
            .build();
    }
}