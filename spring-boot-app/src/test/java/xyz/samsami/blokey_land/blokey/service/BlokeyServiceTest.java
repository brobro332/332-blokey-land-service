package xyz.samsami.blokey_land.blokey.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqUpdateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.discipline.domain.BlokeyDiscipline;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.service.BlokeyDisciplineService;
import xyz.samsami.blokey_land.discipline.service.DisciplineService;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.service.BlokeySkillService;
import xyz.samsami.blokey_land.skill.service.SkillService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlokeyServiceTest {
    @InjectMocks BlokeyService service;
    @Mock BlokeyRepository repository;
    @Mock SkillService skillService;
    @Mock DisciplineService disciplineService;
    @Mock BlokeySkillService blokeySkillService;
    @Mock BlokeyDisciplineService blokeyDisciplineService;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Skill skill1;
    Skill skill2;
    BlokeySkill blokeySkill1;
    BlokeySkill blokeySkill2;

    Discipline discipline1;
    Discipline discipline2;
    BlokeyDiscipline blokeyDiscipline1;
    BlokeyDiscipline blokeyDiscipline2;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = Blokey.builder()
            .id(blokeyId)
            .nickname(nickname)
            .bio(bio)
            .build();

        skill1 = Skill.builder().id(1L).name("java").displayName("Java").build();
        skill2 = Skill.builder().id(2L).name("python").displayName("Python").build();

        blokeySkill1 = BlokeySkill.builder().blokey(blokey).skill(skill1).build();
        blokeySkill2 = BlokeySkill.builder().blokey(blokey).skill(skill2).build();
        blokey.addSkill(blokeySkill1);
        blokey.addSkill(blokeySkill2);

        discipline1 = Discipline.builder().id(1L).name("프론트엔드 개발").build();
        discipline2 = Discipline.builder().id(2L).name("백엔드 개발").build();
        blokeyDiscipline1 = BlokeyDiscipline.builder().blokey(blokey).discipline(discipline1).build();
        blokeyDiscipline2 = BlokeyDiscipline.builder().blokey(blokey).discipline(discipline2).build();
        blokey.addDiscipline(blokeyDiscipline1);
        blokey.addDiscipline(blokeyDiscipline2);
    }

    @DisplayName("유효한 파라미터가 주어진다면_사용자를 생성할 때_모든 필드 값이 저장돼야 한다.")
    @Test
    void givenValidParameter_whenCreateBlokey_thenAllFieldsShouldBeSaved() {
        // given
        ArgumentCaptor<Blokey> captor = ArgumentCaptor.forClass(Blokey.class);
        BlokeyReqCreateDto dto = BlokeyReqCreateDto.builder()
            .id(blokeyId)
            .nickname(nickname)
            .bio(bio)
            .skills(List.of(1L, 2L))
            .disciplines(List.of(1L, 2L))
            .build();

        doNothing().when(blokeySkillService).create(any(), any());
        doNothing().when(blokeyDisciplineService).create(any(), any());

        // when
        service.createBlokey(dto);

        // then
        verify(repository).save(captor.capture());
        Blokey saved = captor.getValue();

        assertEquals(blokeyId, saved.getId());
        assertEquals(nickname, saved.getNickname());
        assertEquals(bio, saved.getBio());
        verify(blokeySkillService, times(2)).create(any(), any());
        verify(blokeyDisciplineService, times(2)).create(any(), any());
    }

    @DisplayName("존재하는 ID가 주어진다면_사용자를 조회할 때_해당 사용자 정보를 반환해야 한다.")
    @Test
    void givenExistingId_whenFindBlokeyByBlokeyId_thenReturnsBlokeys() {
        // given
        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        Blokey result = service.findBlokeyByBlokeyId(blokeyId);

        // then
        assertEquals(blokey.getId(), result.getId());
        assertEquals(blokey.getNickname(), result.getNickname());
        assertEquals(blokey.getBio(), result.getBio());
        assertEquals(2, result.getSkills().size());
        assertEquals(2, result.getDisciplines().size());
        verify(repository).findById(blokeyId);
    }

    @DisplayName("존재하지 않는 ID가 주어진다면_사용자 조회할 때_예외가 발생해야 한다.")
    @Test
    void givenNonExistingId_whenFindBlokeyByBlokeyId_thenThrowException() {
        // given
        UUID nonExistingId = UUID.randomUUID();

        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findBlokeyByBlokeyId(nonExistingId));
    }

    @DisplayName("존재하는 ID가 주어진다면_사용자를 조회할 때_응답 객체를 반환해야 한다.")
    @Test
    void givenValidId_whenReadBlokeyByBlokeyId_thenReturnsBlokey() {
        // given
        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        BlokeyRespDto result = service.readBlokeyByBlokeyId(blokeyId);

        // then
        assertEquals(blokeyId, result.getId());
        assertEquals(nickname, result.getNickname());
        assertEquals(bio, result.getBio());
        assertFalse(result.isHasPendingOffer());
        assertEquals(2, result.getSkills().size());
        assertEquals(2, result.getDisciplines().size());
    }

    @Test
    @DisplayName("유효한 파라미터가 주어진다면_사용자 정보를 수정할 때_갱신 메서드가 호출되어야 한다.")
    void givenValidParameter_whenUpdateBlokeyByBlokeyId_thenCallsUpdateMethods() {
        // given
        UUID newId = UUID.randomUUID();
        String newNickname = "테스트_수정_닉네임_텍스트";
        String newBio = "테스트_수정_소개_텍스트";
        List<Long> newSkills = List.of(2L, 3L);
        List<Long> newDisciplines = List.of(2L, 3L);

        BlokeyReqUpdateDto dto = new BlokeyReqUpdateDto(newNickname, newBio, newSkills, newDisciplines);

        Blokey spy = spy(Blokey.builder()
            .id(newId)
            .nickname(nickname)
            .bio(bio)
            .build());

        when(spy.getSkills()).thenReturn(Set.of(blokeySkill1, blokeySkill2));
        when(spy.getDisciplines()).thenReturn(Set.of(blokeyDiscipline1, blokeyDiscipline2));
        when(repository.findById(newId)).thenReturn(Optional.of(spy));

        when(skillService.findSkillBySkillId(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(skill1.getId())) return skill1;
            else if (id.equals(skill2.getId())) return skill2;
            else return Skill.builder().id(id).name("skill" + id).displayName("Skill " + id).build();
        });

        when(disciplineService.findDisciplineByDisciplineId(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            if (id.equals(discipline1.getId())) return discipline1;
            else if (id.equals(discipline2.getId())) return discipline2;
            else return Discipline.builder().id(id).name("discipline" + id).build();
        });

        doNothing().when(blokeySkillService).create(any(Blokey.class), any(Skill.class));
        doNothing().when(blokeySkillService).delete(any(Blokey.class), any(Skill.class));
        doNothing().when(blokeyDisciplineService).create(any(Blokey.class), any(Discipline.class));
        doNothing().when(blokeyDisciplineService).delete(any(Blokey.class), any(Discipline.class));

        // when
        service.updateBlokeyByBlokeyId(newId, dto);

        // then
        verify(repository).findById(newId);
        verify(spy).updateNickname(newNickname);
        verify(spy).updateBio(newBio);
        verify(blokeySkillService, atLeastOnce()).create(eq(spy), any(Skill.class));
        verify(blokeySkillService, atLeastOnce()).delete(eq(spy), any(Skill.class));
        verify(blokeyDisciplineService, atLeastOnce()).create(eq(spy), any(Discipline.class));
        verify(blokeyDisciplineService, atLeastOnce()).delete(eq(spy), any(Discipline.class));
    }

    @Test
    @DisplayName("존재하는 ID가 주어진다면_사용자 삭제할 때_삭제 메서드가 호출되어야 한다.")
    void deleteBlokey_whenDeleteBlokeyByBlokeyId_thenCallsMethod() {
        // given
        UUID id = UUID.randomUUID();
        Blokey mock = mock(Blokey.class);

        when(repository.findById(id)).thenReturn(Optional.of(mock));

        // when
        service.deleteBlokeyByBlokeyId(id);

        // then
        verify(repository).delete(mock);
    }
}