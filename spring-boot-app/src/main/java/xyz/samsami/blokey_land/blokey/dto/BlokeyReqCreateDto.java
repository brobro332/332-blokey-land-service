package xyz.samsami.blokey_land.blokey.dto;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BlokeyReqCreateDto {
    private UUID id;
    private String nickname;
    private String bio;
}