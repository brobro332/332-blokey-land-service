package xyz.samsami.blokey_land.matching.common.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import xyz.samsami.blokey_land.common.event.EmbeddingRequestedEvent;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.EntityType;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.matching.embedding.service.EmbeddingService;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequestEventHandler {
    private final EmbeddingService embeddingService;
    /* TODO: 색인 서비스 구현 필요 */
    // private final IndexingService indexingService;

    @EventListener
    public void handle(EmbeddingRequestedEvent event) {
        List<Float> vector = embedEntity(event.getEntityType(), event.getEntityId());

        /* TODO: 색인 메서드 구현 필요 */
        // indexingService.index(event.getEntityType(), event.getEntityId(), vector);
    }

    private List<Float> embedEntity(EntityType entityType, Serializable entityId) {
        switch (entityType) {
            case BLOKEY -> { return embeddingService.embedBlokey((UUID) entityId); }
            case PROJECT -> { return embeddingService.embedProject((Long) entityId); }
            default -> throw new CommonException(ExceptionType.INTERNAL_SERVER_ERROR, null);
        }
    }
}
