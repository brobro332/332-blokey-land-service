package kr.co.co_working.invitation.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvitationResponseDto {
    private String division;
    private String email;
    private String name;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @QueryProjection
    public InvitationResponseDto(String division, String email, String name, String description, String status, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.division = division;
        this.email = email;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
