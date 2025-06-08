package xyz.samsami.blokey_land.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRespDto {
    private UUID id;
    private String nickname;
    private String bio;
}