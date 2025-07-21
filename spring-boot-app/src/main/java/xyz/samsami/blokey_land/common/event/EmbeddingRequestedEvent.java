package xyz.samsami.blokey_land.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import xyz.samsami.blokey_land.common.type.EntityType;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
public class EmbeddingRequestedEvent {
    private EntityType entityType;
    private Serializable entityId;
}

