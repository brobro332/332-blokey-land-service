package xyz.samsami.blokey_land.project.mapper;

import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;

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

    public static ProjectRespDto toRespDto(Project project) {
        return ProjectRespDto.builder()
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

    public static ProjectRespDto toRespDtoWithIsLeader(Project project, boolean isLeader) {
        return ProjectRespDto.builder()
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
}