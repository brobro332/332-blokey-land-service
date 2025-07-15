package xyz.samsami.blokey_land.project.mapper;

import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;
import xyz.samsami.blokey_land.project.dto.ProjectWithTaskResponseDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;

import java.util.List;

public class ProjectMapper {
    public static Project toEntity(ProjectReqCreateDto dto) {
        return Project.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .imageUrl(dto.getImageUrl())
            .status(dto.getStatus())
            .isPrivate(dto.isPrivate())
            .estimatedStartDate(dto.getEstimatedStartDate())
            .estimatedEndDate(dto.getEstimatedEndDate())
            .actualStartDate(dto.getActualStartDate())
            .actualEndDate(dto.getActualEndDate())
            .build();
    }

    public static ProjectOnlyRespDto toRespDto(Project project) {
        return ProjectOnlyRespDto.builder()
            .id(project.getId())
            .title(project.getTitle())
            .description(project.getDescription())
            .imageUrl(project.getImageUrl())
            .status(project.getStatus())
            .isPrivate(project.isPrivate())
            .estimatedStartDate(project.getEstimatedStartDate())
            .estimatedEndDate(project.getEstimatedEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .build();
    }

    public static ProjectOnlyRespDto toRespDtoWithIsLeader(Project project, boolean isLeader) {
        return ProjectOnlyRespDto.builder()
            .id(project.getId())
            .title(project.getTitle())
            .description(project.getDescription())
            .imageUrl(project.getImageUrl())
            .status(project.getStatus())
            .isPrivate(project.isPrivate())
            .isLeader(isLeader)
            .estimatedStartDate(project.getEstimatedStartDate())
            .estimatedEndDate(project.getEstimatedEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .build();
    }

    public static ProjectWithTaskResponseDto toRespDtoWithTaskDtoList(Project project, List<TaskRespDto> taskRespDtoList) {
        return ProjectWithTaskResponseDto.builder()
            .id(project.getId())
            .title(project.getTitle())
            .description(project.getDescription())
            .imageUrl(project.getImageUrl())
            .status(project.getStatus())
            .isPrivate(project.isPrivate())
            .estimatedStartDate(project.getEstimatedStartDate())
            .estimatedEndDate(project.getEstimatedEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .tasks(taskRespDtoList)
            .build();
    }
}