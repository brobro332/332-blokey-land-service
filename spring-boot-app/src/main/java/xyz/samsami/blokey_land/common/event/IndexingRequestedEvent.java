package xyz.samsami.blokey_land.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import xyz.samsami.blokey_land.common.type.EntityType;

import java.io.Serializable;

@AllArgsConstructor
@Builder
public class IndexingRequestedEvent {
    private final EntityType entityType;
    private final Serializable entityId;
}
