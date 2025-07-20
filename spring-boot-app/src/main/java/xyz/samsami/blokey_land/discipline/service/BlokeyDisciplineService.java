package xyz.samsami.blokey_land.discipline.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.service.AbstractRelationService;
import xyz.samsami.blokey_land.discipline.domain.BlokeyDiscipline;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.repository.BlokeyDisciplineRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlokeyDisciplineService extends AbstractRelationService<BlokeyDiscipline, Blokey, Discipline> {
    private final BlokeyDisciplineRepository repository;

    @Override
    protected BlokeyDiscipline createRelation(Blokey blokey, Discipline Discipline) {
        return BlokeyDiscipline.builder().blokey(blokey).discipline(Discipline).build();
    }

    @Override
    protected Optional<BlokeyDiscipline> findRelation(Blokey blokey, Discipline Discipline) {
        return Optional.ofNullable(repository.findByBlokeyAndDiscipline(blokey, Discipline));
    }

    @Override
    protected void saveRelation(BlokeyDiscipline relation) {
        relation.getBlokey().addDiscipline(relation);
        repository.save(relation);
    }

    @Override
    protected void deleteRelation(BlokeyDiscipline relation) {
        relation.getBlokey().removeDiscipline(relation);
        repository.delete(relation);
    }

    public List<BlokeyDiscipline> findByBlokeyIdIn(Set<UUID> blokeyIds) {
        return repository.findByBlokeyIdIn(blokeyIds);
    }
}
