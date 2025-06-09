package xyz.samsami.blokey_land.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRespDto {
    private String title;
    private String description;
    private UUID ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
}