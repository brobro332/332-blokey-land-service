package xyz.samsami.blokey_land.discipline.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.repository.DisciplineRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DisciplineService {
    private final DisciplineRepository repository;

    public Discipline findDisciplineByDisciplineId(Long disciplineId) {
        return repository.findById(disciplineId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}
