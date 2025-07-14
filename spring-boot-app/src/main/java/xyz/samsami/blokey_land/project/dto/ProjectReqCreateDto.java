package xyz.samsami.blokey_land.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Size(min = 2, max = 100, message = "프로젝트 제목은 2자 이상 100자 이하로 입력해주세요.")
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