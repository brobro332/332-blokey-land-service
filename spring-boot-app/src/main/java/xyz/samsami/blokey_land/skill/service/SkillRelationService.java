package xyz.samsami.blokey_land.skill.service;

import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.skill.domain.RelationTarget;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.domain.SkillRelation;

import java.util.Optional;

public abstract class SkillRelationService<R extends SkillRelation, T extends RelationTarget> {

    protected abstract R createRelation(T target, Skill skill);
    protected abstract Optional<R> findRelation(T target, Skill skill);
    protected abstract void saveRelation(R relation);
    protected abstract void deleteRelation(R relation);

    @Transactional
    public void create(T target, Skill skill) {
        if (findRelation(target, skill).isPresent()) return;
        saveRelation(createRelation(target, skill));
    }

    @Transactional
    public void delete(T target, Skill skill) {
        R relation = findRelation(target, skill)
            .orElseThrow(() -> new CommonException(ExceptionType.NOT_FOUND, null));
        deleteRelation(relation);
    }
}