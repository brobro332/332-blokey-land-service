package xyz.samsami.blokey_land.milestone.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MilestoneReqReadDto {
    private String title;
    private String description;
    private Integer month;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;
    private Long projectId;
}
