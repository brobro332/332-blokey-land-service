package xyz.samsami.blokey_land.member.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    LEADER("리더"),
    MANAGER("매니저"),
    MEMBER("멤버"),
    VIEWER("뷰어");

    private final String description;
}