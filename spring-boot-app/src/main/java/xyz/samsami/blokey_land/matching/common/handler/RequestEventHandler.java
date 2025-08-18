package xyz.samsami.blokey_land.matching.common.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.samsami.blokey_land.common.event.EmbeddingRequestedEvent;
import xyz.samsami.blokey_land.common.event.IndexingRequestedEvent;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.EntityType;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.matching.embedding.service.EmbeddingService;
import xyz.samsami.blokey_land.matching.indexing.service.IndexingService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequestEventHandler {
    private final EmbeddingService embeddingService;
    private final IndexingService indexingService;

    @EventListener
    public void handle(EmbeddingRequestedEvent event) {
        List<Float> vector = embedEntity(event.getEntityType(), event.getEntityId());
        indexingService.index(event.getEntityType(), event.getEntityId(), vector);
    }

    @EventListener
    public void handle(IndexingRequestedEvent event) {
        indexingService.index(event.getEntityType(), event.getEntityId(), null);
    }

    private List<Float> embedEntity(EntityType entityType, Serializable entityId) {
        switch (entityType) {
            case BLOKEY -> { return embeddingService.embedBlokey((UUID) entityId); }
            case PROJECT -> { return embeddingService.embedProject((Long) entityId); }
            default -> throw new CommonException(ExceptionType.INTERNAL_SERVER_ERROR, null);
        }
    }
}
