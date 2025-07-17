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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlokeyServiceTest {
    @InjectMocks private BlokeyService service;
    @Mock private BlokeyRepository repository;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = Blokey.builder()
            .id(blokeyId)
            .nickname(nickname)
            .bio(bio)
            .build();
    }

    @DisplayName("사용자를 생성할 때 모든 필수 값이 저장돼야 한다.")
    @Test
    void givenValidBlokeyDto_whenCreateBlokey_thenAllFieldsShouldBeSaved() {
        // given
        BlokeyReqCreateDto dto = BlokeyReqCreateDto.builder()
            .id(blokeyId)
            .nickname(nickname)
            .bio(bio)
            .build();

        ArgumentCaptor<Blokey> captor = ArgumentCaptor.forClass(Blokey.class);

        // when
        service.createBlokey(dto);

        // then
        verify(repository).save(captor.capture());
        Blokey saved = captor.getValue();

        assertEquals(blokeyId, saved.getId());
        assertEquals(nickname, saved.getNickname());
        assertEquals(bio, saved.getBio());
    }

    @DisplayName("존재하는 ID로 사용자를 조회하면 해당 객체를 반환해야 한다.")
    @Test
    void givenValidId_whenFindBlokeyByBlokeyId_thenReturnBlokey() {
        // given
        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        Blokey found = service.findBlokeyByBlokeyId(blokeyId);

        // then
        assertEquals(blokey.getId(), found.getId());
        assertEquals(blokey.getNickname(), found.getNickname());
        assertEquals(blokey.getBio(), found.getBio());

        verify(repository).findById(blokeyId);
    }

    @DisplayName("존재하지 않는 ID로 사용자 조회 시 예외가 발생해야 한다.")
    @Test
    void givenInvalidId_whenFindBlokeyByBlokeyId_thenThrowException() {
        // given
        UUID undefinedId = UUID.randomUUID();

        when(repository.findById(undefinedId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> {
            service.findBlokeyByBlokeyId(undefinedId);
        });
    }

    @DisplayName("존재하는 ID로 사용자를 조회하면 DTO로 변환하여 반환해야 한다.")
    @Test
    void givenValidId_whenReadBlokeyByBlokeyId_thenReturnDto() {
        // given
        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        BlokeyRespDto dto = service.readBlokeyByBlokeyId(blokeyId);

        // then
        assertEquals(blokeyId, dto.getId());
        assertEquals(nickname, dto.getNickname());
        assertEquals(bio, dto.getBio());
        assertFalse(dto.isHasPendingOffer());
    }

    @Test
    @DisplayName("닉네임 또는 소개가 있으면 상태 변경 메서드가 호출되어야 한다.")
    void updateBlokey_whenUpdateBlokeyByBlokeyId_thenCallStateChangingMethod() {
        // given
        UUID newId = UUID.randomUUID();
        String newNickname = "테스트_수정_닉네임_텍스트";
        String newBio = "테스트_수정_소개_텍스트";

        BlokeyReqUpdateDto dto = new BlokeyReqUpdateDto(newNickname, newBio);
        Blokey mock = mock(Blokey.class);

        when(repository.findById(newId)).thenReturn(Optional.of(mock));

        // when
        service.updateBlokeyByBlokeyId(newId, dto);

        // then
        verify(repository).findById(newId);
        verify(mock).updateNickname(newNickname);
        verify(mock).updateBio(newBio);
    }

    @Test
    @DisplayName("존재하는 사용자 삭제 시 삭제 메서드 호출되어야 한다.")
    void deleteBlokey_whenDeleteBlokeyByBlokeyId_thenCallRepositoryDeleteMethod() {
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