package xyz.samsami.blokey_land.task.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PriorityType {
    LOW("낮음"),
    MEDIUM("보통"),
    HIGH("높음");

    private final String description;
}
