package xyz.samsami.blokey_land.discipline.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.discipline.domain.BlokeyDiscipline;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.repository.BlokeyDisciplineRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlokeyDisciplineServiceTest {
    @InjectMocks
    private BlokeyDisciplineService service;
    @Mock
    private BlokeyDisciplineRepository repository;

    private Blokey blokey;
    private Discipline discipline;
    private BlokeyDiscipline relation;

    @BeforeEach
    void setUp() {
        blokey = Blokey.builder().id(UUID.randomUUID()).build();
        discipline = Discipline.builder().id(1L).name("백엔드").build();
        relation = BlokeyDiscipline.builder().blokey(blokey).discipline(discipline).build();
    }

    @Test
    @DisplayName("사용자와 분야 엔티티가 주어진다면_연관관계를 생성할 때_객체가 생성돼야 한다.")
    void givenBlokeyAndDiscipline_whenCreateRelation_thenShouldReturnsCorrectEntity() {
        // when
        BlokeyDiscipline blokeyDiscipline = service.createRelation(blokey, discipline);

        // then
        assertEquals(blokey, blokeyDiscipline.getBlokey());
        assertEquals(discipline, blokeyDiscipline.getDiscipline());
    }

    @Test
    @DisplayName("사용자와 분야 연관관계가 존재한다면_연관관계를 조회할 때_결과가 반환돼야 한다.")
    void givenExistingRelation_whenFindRelation_thenShouldReturnOptional() {
        // given
        when(repository.findByBlokeyAndDiscipline(blokey, discipline)).thenReturn(relation);

        // when
        Optional<BlokeyDiscipline> result = service.findRelation(blokey, discipline);

        // then
        assertTrue(result.isPresent());
        assertEquals(relation, result.get());
    }

    @Test
    @DisplayName("사용자와 분야 연관관계가 존재하지 않을 때_연관관계를 조회할 때_조회 결과가 없어야 한다.")
    void givenNoRelation_whenFindRelation_thenShouldReturnEmptyOptional() {
        // given
        when(repository.findByBlokeyAndDiscipline(blokey, discipline)).thenReturn(null);

        // when
        Optional<BlokeyDiscipline> result = service.findRelation(blokey, discipline);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("연관관계를 주어졌을 때_연관관계를 저장할 때_올바르게 저장되어야 한다.")
    void givenRelation_whenSaveRelation_thenShouldAddToBlokeyAndSaveToRepository() {
        // given
        Blokey spy = spy(blokey);
        BlokeyDiscipline relation = BlokeyDiscipline.builder()
            .blokey(spy)
            .discipline(discipline)
            .build();

        // when
        service.saveRelation(relation);

        // then
        verify(spy).addDiscipline(relation);
        verify(repository).save(relation);
    }

    @Test
    @DisplayName("연관관계가 주어졌다면_연관관계를 삭제할 때_연관관계가 제거되어야 한다.")
    void givenRelation_whenDeleteRelation_thenShouldRemoveFromBlokeyAndDeleteFromRepository() {
        // given
        Blokey spy = spy(blokey);
        BlokeyDiscipline relation = BlokeyDiscipline.builder()
            .blokey(spy)
            .discipline(discipline)
            .build();

        // when
        service.deleteRelation(relation);

        // then
        verify(spy).removeDiscipline(relation);
        verify(repository).delete(relation);
    }
}