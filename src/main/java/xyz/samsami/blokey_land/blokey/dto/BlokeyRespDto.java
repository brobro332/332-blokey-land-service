package xyz.samsami.blokey_land.blokey.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlokeyRespDto {
    private UUID id;
    private String nickname;
    private String bio;
}