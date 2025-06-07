package xyz.samsami.blokey_land.common.type;

import lombok.Getter;

public enum ResultType {
    SUCCESS("성공"),
    FAIL("실패");

    @Getter
    private final String description;

    ResultType(String description) {
        this.description = description;
    }
}
