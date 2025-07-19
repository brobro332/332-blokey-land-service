package xyz.samsami.blokey_land.blokey.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BlokeyReqUpdateDto {
    @Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하로 입력해주세요.")
    private String nickname;

    @Size(max = 200, message = "소개는 200자 이하로 입력해주세요.")
    private String bio;

    private List<Long> skills;
}