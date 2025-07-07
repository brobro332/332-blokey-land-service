package xyz.samsami.blokey_land.project.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatusType {
    ACTIVE("진행중"),
    COMPLETED("완료"),
    DELETED("삭제");

    private final String description;
}
