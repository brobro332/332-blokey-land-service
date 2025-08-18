package xyz.samsami.blokey_land.matching.indexing.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.EntityType;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.matching.indexing.document.BlokeyDocument;
import xyz.samsami.blokey_land.matching.indexing.document.ProjectDocument;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndexingService {
    private final BlokeyRepository blokeyRepository;
    private final ProjectRepository projectRepository;
    private final ElasticsearchOperations operations;

    @Transactional
    public void index(EntityType type, Serializable id, List<Float> vector) {
        switch (type) {
            case BLOKEY -> {
                Blokey blokey = blokeyRepository.findById((UUID) id)
                    .orElseThrow(() -> new CommonException(ExceptionType.NOT_FOUND, "Blokey not found"));
                indexBlokeyWithVector(blokey, vector);
            }
            case PROJECT -> {
                Project project = projectRepository.findById((Long) id)
                    .orElseThrow(() -> new CommonException(ExceptionType.NOT_FOUND, "Project not found"));
                indexProjectWithVector(project, vector);
            }
            default -> throw new CommonException(ExceptionType.INTERNAL_SERVER_ERROR, null);
        }
    }

    public void indexBlokeyWithVector(Blokey blokey, List<Float> vector) {
        if (vector != null) {
            BlokeyDocument document = BlokeyDocument.toDocument(blokey, toFloatArray(vector));
            operations.save(document);
        } else {
            BlokeyDocument existing = operations.get(blokey.getId().toString(), BlokeyDocument.class);
            if (existing == null) return;

            BlokeyDocument updated = BlokeyDocument.toDocument(blokey, existing.getEmbedding());
            operations.save(updated);
        }
    }

    public void indexProjectWithVector(Project project, List<Float> vector) {
        if (vector != null) {
            ProjectDocument document = ProjectDocument.toDocument(project, toFloatArray(vector));
            operations.save(document);
        } else {
            ProjectDocument existing = operations.get(project.getId().toString(), ProjectDocument.class);
            if (existing == null) return;

            ProjectDocument updated = ProjectDocument.toDocument(project, existing.getEmbedding());
            operations.save(updated);
        }
    }

    private float[] toFloatArray(List<Float> list) {
        float[] result = new float[list.size()];
        for (int i = 0; i < list.size(); i++) result[i] = list.get(i);
        return result;
    }
}
