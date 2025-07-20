package xyz.samsami.blokey_land.blokey.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BlokeyReqCreateDto {
    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    @Size(max = 200, message = "소개는 200자 이하로 입력해주세요.")
    private String bio;

    private List<Long> skills;
    private List<Long> disciplines;
}