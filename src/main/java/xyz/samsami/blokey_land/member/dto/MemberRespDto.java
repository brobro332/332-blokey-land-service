package xyz.samsami.blokey_land.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.member.type.RoleType;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRespDto {
    private Long memberId;
    private RoleType role;
    private Long projectId;
    private UUID userId;
}