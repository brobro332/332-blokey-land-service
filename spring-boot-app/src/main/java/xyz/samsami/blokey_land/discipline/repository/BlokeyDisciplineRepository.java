package xyz.samsami.blokey_land.discipline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.discipline.domain.BlokeyDiscipline;
import xyz.samsami.blokey_land.discipline.domain.Discipline;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BlokeyDisciplineRepository extends JpaRepository<BlokeyDiscipline, Long> {
    BlokeyDiscipline findByBlokeyAndDiscipline(Blokey blokey, Discipline discipline);
    List<BlokeyDiscipline> findByBlokeyIdIn(Set<UUID> blokeyIds);
}
