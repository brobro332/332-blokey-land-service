package xyz.samsami.blokey_land.project.dto;

import lombok.*;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ProjectReqReadDto {
    private String title;
    private String description;
    private LocalDate actualStartFrom;
    private LocalDate actualEndTo;
    private ProjectStatusType status;
    private Boolean isPrivate;
}
