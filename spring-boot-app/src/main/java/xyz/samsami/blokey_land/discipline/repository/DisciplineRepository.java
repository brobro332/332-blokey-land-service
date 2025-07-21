package xyz.samsami.blokey_land.discipline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.discipline.domain.Discipline;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}
