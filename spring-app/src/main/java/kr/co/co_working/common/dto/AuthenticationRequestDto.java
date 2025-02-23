package kr.co.co_working.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuthenticationRequestDto {
    @Getter
    @Setter
    public static class LOGIN {
        private String email;
        private String password;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CHECK {
        private String password;
    }
}
