package xyz.samsami.blokey_land.project.mapper;

import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;

public class ProjectMapper {
    public static Project toEntity(ProjectReqCreateDto dto) {
        return Project.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .ownerId(dto.getOwnerId())
            .estimatedStartDate(dto.getEstimatedStartDate())
            .estimatedEndDate(dto.getEstimatedEndDate())
            .actualStartDate(dto.getActualStartDate())
            .actualEndDate(dto.getActualEndDate())
            .build();
    }

    public static ProjectRespDto toRespDto(Project project) {
        return ProjectRespDto.builder()
            .id(project.getId())
            .title(project.getTitle())
            .description(project.getDescription())
            .ownerId(project.getOwnerId())
            .estimatedStartDate(project.getEstimatedStartDate())
            .estimatedEndDate(project.getEstimatedEndDate())
            .actualStartDate(project.getActualStartDate())
            .actualEndDate(project.getActualEndDate())
            .build();
    }
}