package xyz.samsami.blokey_land.project.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReqUpdateDto {
    @Size(min = 2, max = 100, message = "프로젝트 제목은 2자 이상 100자 이하로 입력해주세요.")
    private String title;

    private String description;
    private String imageUrl;
    private ProjectStatusType status;
    private Boolean isPrivate;
    private List<Long> skills;
    private List<Long> disciplines;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
}