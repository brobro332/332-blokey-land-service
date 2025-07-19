package xyz.samsami.blokey_land.skill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.repository.BlokeySkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlokeySkillService extends SkillRelationService<BlokeySkill, Blokey> {
    private final BlokeySkillRepository repository;

    @Override
    protected BlokeySkill createRelation(Blokey blokey, Skill skill) {
        return BlokeySkill.builder().blokey(blokey).skill(skill).build();
    }

    @Override
    protected Optional<BlokeySkill> findRelation(Blokey blokey, Skill skill) {
        return Optional.ofNullable(repository.findByBlokeyAndSkill(blokey, skill));
    }

    @Override
    protected void saveRelation(BlokeySkill relation) {
        relation.getBlokey().addSkill(relation);
        repository.save(relation);
    }

    @Override
    protected void deleteRelation(BlokeySkill relation) {
        relation.getBlokey().removeSkill(relation);
        repository.delete(relation);
    }

    public List<BlokeySkill> findByBlokeyIdIn(Set<UUID> blokeyIds) {
        return repository.findByBlokeyIdIn(blokeyIds);
    }
}
