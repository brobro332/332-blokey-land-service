package kr.co.co_working.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberResponseDto {
        private String email;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String leader;

        @QueryProjection
        public MemberResponseDto(String email, String name, String description, LocalDateTime createdAt, LocalDateTime modifiedAt) {
                this.email = email;
                this.name = name;
                this.description = description;
                this.createdAt = createdAt;
                this.modifiedAt = modifiedAt;
        }

        @QueryProjection
        public MemberResponseDto(String email, String name, String description, LocalDateTime createdAt, LocalDateTime modifiedAt, String leader) {
                this.email = email;
                this.name = name;
                this.description = description;
                this.createdAt = createdAt;
                this.modifiedAt = modifiedAt;
                this.leader = leader;
        }
}
