package xyz.samsami.blokey_land.common.service;

import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.domain.RelationTarget;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;

import java.util.Optional;

public abstract class AbstractRelationService<R, T extends RelationTarget, C> {

    protected abstract R createRelation(T target, C connectable); // connectable: Skill, Discipline 등
    protected abstract Optional<R> findRelation(T target, C connectable);
    protected abstract void saveRelation(R relation);
    protected abstract void deleteRelation(R relation);

    @Transactional
    public void create(T target, C connectable) {
        if (findRelation(target, connectable).isPresent()) return;
        saveRelation(createRelation(target, connectable));
    }

    @Transactional
    public void delete(T target, C connectable) {
        R relation = findRelation(target, connectable)
            .orElseThrow(() -> new CommonException(ExceptionType.NOT_FOUND, null));
        deleteRelation(relation);
    }
}