package xyz.samsami.blokey_land.milestone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneRespDto {
    private Long milestoneId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Long projectId;
}