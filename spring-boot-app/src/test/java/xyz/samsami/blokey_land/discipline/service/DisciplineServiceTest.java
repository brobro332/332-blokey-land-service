package xyz.samsami.blokey_land.discipline.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.repository.DisciplineRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisciplineServiceTest {
    @InjectMocks private DisciplineService service;
    @Mock private DisciplineRepository repository;

    @Test
    @DisplayName("존재하는 ID가 주어진다면_분야를 조회할 때_올바르게 조회해야 한다.")
    void givenExistingId_whenFindDisciplineByDisciplineId_thenReturnsDiscipline() {
        // given
        Long id = 1L;
        Discipline discipline = Discipline.builder().id(id).name("백엔드").build();
        when(repository.findById(id)).thenReturn(Optional.of(discipline));

        // when
        Discipline result = service.findDisciplineByDisciplineId(id);

        // then
        assertThat(result).isEqualTo(discipline);
    }

    @Test
    @DisplayName("존재하지 않는 ID가 주어진다면_분야를 조회할 때_예외를 발생시켜야 한다.")
    void givenNonExistingId_whenFindDisciplineByDisciplineId_thenThrowsException() {
        // given
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // when & then
        CommonException exception = assertThrows(CommonException.class, () ->
            service.findDisciplineByDisciplineId(id)
        );

        Assertions.assertEquals(ExceptionType.NOT_FOUND, exception.getException());
    }
}