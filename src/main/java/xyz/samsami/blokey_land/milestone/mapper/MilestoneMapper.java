package xyz.samsami.blokey_land.milestone.mapper;

import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.project.domain.Project;

public class MilestoneMapper {
    public static Milestone toEntity(MilestoneReqCreateDto dto, Project project) {
        return Milestone.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .dueDate(dto.getDueDate())
            .project(project)
            .build();
    }
}