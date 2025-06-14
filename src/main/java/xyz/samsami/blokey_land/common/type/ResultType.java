package xyz.samsami.blokey_land.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultType {
    SUCCESS("성공"),
    FAIL("실패");

    private final String description;
}
