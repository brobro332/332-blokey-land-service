package xyz.samsami.blokey_land.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReqCreateDto {
    private String title;
    private String description;
    private String imageUrl;
    private final ProjectStatusType status = ProjectStatusType.ACTIVE;
    private boolean isPrivate;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
}