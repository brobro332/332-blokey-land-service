package kr.co.co_working.common.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthenticationRequestDto {
    @Getter
    @Setter
    public static class LOGIN {
        private String email;
        private String password;
    }
}
