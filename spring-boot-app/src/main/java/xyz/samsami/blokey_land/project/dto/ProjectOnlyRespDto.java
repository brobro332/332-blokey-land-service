package xyz.samsami.blokey_land.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProjectOnlyRespDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private ProjectStatusType status;
    private boolean isPrivate;
    private boolean isLeader;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    @JsonProperty("isLeader")
    public boolean getIsLeader() {
        return isLeader;
    }
}