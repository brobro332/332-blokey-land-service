package xyz.samsami.blokey_land.milestone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneReqCreateDto {
    @NotBlank
    @Size(min = 2, max = 100, message = "마일스톤 제목은 2자 이상 100자 이하로 입력해주세요.")
    private String title;

    @NotNull
    private LocalDate dueDate;

    private String description;
}