package xyz.samsami.blokey_land.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserReqCreateDto {
    private String email;
    private String password;
    private String nickname;
    private String bio;
}