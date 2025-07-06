package xyz.samsami.blokey_land.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.member.type.RoleType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberReqUpdateDto {
    private Long memberId;
    private RoleType role;
}