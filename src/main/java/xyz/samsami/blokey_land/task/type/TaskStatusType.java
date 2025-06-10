package xyz.samsami.blokey_land.task.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatusType {
    TODO("시작전"),
    IN_PROGRESS("진행중"),
    REVIEW("검토중"),
    DONE("완료");

    private final String description;
}