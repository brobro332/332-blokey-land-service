package xyz.samsami.blokey_land.blokey.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BlokeyReqCreateDto {
    private String email;
    private String password;
    private String nickname;
    private String bio;
}