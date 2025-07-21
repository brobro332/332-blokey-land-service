package xyz.samsami.blokey_land.skill.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.repository.BlokeySkillRepository;

import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlokeySkillServiceTest {
    @InjectMocks BlokeySkillService blokeySkillService;
    @Mock BlokeySkillRepository blokeySkillRepository;

    Blokey blokey;
    Skill skill;

    @BeforeEach
    void setUp() {
        blokey = Blokey.builder()
            .id(UUID.randomUUID())
            .build();

        skill = Skill.builder()
            .id(1L)
            .build();
    }

    @Test
    @DisplayName("사용자와 스킬이 주어진다면_연관관계를 생성할 때_올바르게 저장돼야 한다.")
    void givenBlokeyAndSkill_whenCreateRelation_thenSaveNewRelation() {
        // given
        when(blokeySkillRepository.findByBlokeyAndSkill(blokey, skill)).thenReturn(null);

        // when
        blokeySkillService.create(blokey, skill);

        // then
        verify(blokeySkillRepository).save(argThat(saved ->
            saved.getBlokey().equals(blokey) &&
                saved.getSkill().equals(skill)
        ));
    }

    @Test
    @DisplayName("이미 존재하는 연관관계가 주어진다면_연관관계를 생성할 때_생성하지 않아야 한다.")
    void givenExistingRelation_whenCreateRelation_thenDoNothing() {
        // given
        BlokeySkill existingRelation = BlokeySkill.builder()
            .blokey(blokey)
            .skill(skill)
            .build();

        when(blokeySkillRepository.findByBlokeyAndSkill(blokey, skill)).thenReturn(existingRelation);

        // when
        blokeySkillService.create(blokey, skill);

        // then
        verify(blokeySkillRepository, never()).save(any());
    }

    @Test
    @DisplayName("연관관계가 주어진다면_연관관계를 삭제할 때_올바르게 제거돼야 한다.")
    void givenExistingRelation_whenDeleteRelation_thenDeleteCalled() {
        // given
        BlokeySkill relation = BlokeySkill.builder()
            .blokey(blokey)
            .skill(skill)
            .build();

        when(blokeySkillRepository.findByBlokeyAndSkill(blokey, skill)).thenReturn(relation);

        // when
        blokeySkillService.delete(blokey, skill);

        // then
        verify(blokeySkillRepository).delete(relation);
    }

    @Test
    @DisplayName("존재하지 않는 연관관계가 주어진다면_연관관계를 삭제할 때_예외를 발생시킨다.")
    void givenNonExistingRelation_whenDeleteRelation_thenThrowsException() {
        // given
        when(blokeySkillRepository.findByBlokeyAndSkill(blokey, skill)).thenReturn(null);

        // when, then
        assertThrows(CommonException.class, () -> blokeySkillService.delete(blokey, skill));
    }
}