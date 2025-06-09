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
            .startDate(dto.getStartDate())
            .endDate(dto.getEndDate())
            .build();
    }

    public static ProjectRespDto toRespDto(Project project) {
        return ProjectRespDto.builder()
            .title(project.getTitle())
            .description(project.getDescription())
            .ownerId(project.getOwnerId())
            .startDate(project.getStartDate())
            .endDate(project.getEndDate())
            .build();
    }
}