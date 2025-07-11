package xyz.samsami.blokey_land.blokey.service;

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
    @InjectMocks private BlokeyService blokeyService;
    @Mock private BlokeyRepository blokeyRepository;

    @DisplayName("Blokey를 생성할 때 모든 필수 값이 저장돼야 한다.")
    @Test
    void givenValidBlokeyDto_whenCreateBlokey_thenAllFieldsShouldBeSaved() {
        // given
        UUID id = UUID.randomUUID();
        String nickname = "nickname";
        String bio = "bio";

        BlokeyReqCreateDto dto = BlokeyReqCreateDto.builder()
            .id(id)
            .nickname(nickname)
            .bio(bio)
            .build();

        ArgumentCaptor<Blokey> captor = ArgumentCaptor.forClass(Blokey.class);

        // when
        blokeyService.createBlokey(dto);

        // then
        verify(blokeyRepository).save(captor.capture());
        Blokey saved = captor.getValue();

        assertEquals(id, saved.getId());
        assertEquals(nickname, saved.getNickname());
        assertEquals(bio, saved.getBio());
    }

    @DisplayName("존재하는 ID로 Blokey를 조회하면 해당 객체를 반환해야 한다.")
    @Test
    void givenValidId_whenFindBlokeyByBlokeyId_thenReturnBlokey() {
        // given
        UUID id = UUID.randomUUID();
        Blokey blokey = new Blokey(id, "nickname", "bio");

        when(blokeyRepository.findById(id)).thenReturn(Optional.of(blokey));

        // when
        Blokey found = blokeyService.findBlokeyByBlokeyId(id);

        // then
        assertEquals(blokey.getId(), found.getId());
        assertEquals(blokey.getNickname(), found.getNickname());
        assertEquals(blokey.getBio(), found.getBio());

        verify(blokeyRepository).findById(id);
    }

    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생해야 한다.")
    @Test
    void givenInvalidId_whenFindBlokeyByBlokeyId_thenThrowException() {
        // given
        UUID nonExistingId = UUID.randomUUID();

        when(blokeyRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> {
            blokeyService.findBlokeyByBlokeyId(nonExistingId);
        });
    }

    @DisplayName("존재하는 ID로 Blokey를 조회하면 DTO로 변환하여 반환해야 한다.")
    @Test
    void givenValidId_whenReadBlokeyByBlokeyId_thenReturnDto() {
        // given
        UUID id = UUID.randomUUID();
        Blokey blokey = new Blokey(id, "nickname", "bio");

        when(blokeyRepository.findById(id)).thenReturn(Optional.of(blokey));

        // when
        BlokeyRespDto dto = blokeyService.readBlokeyByBlokeyId(id);

        // then
        assertEquals(id, dto.getId());
        assertEquals("nickname", dto.getNickname());
        assertEquals("bio", dto.getBio());
        assertFalse(dto.isHasPendingOffer());
    }

    @Test
    @DisplayName("닉네임 또는 소개가 있으면 상태 변경 메서드가 호출되어야 한다.")
    void updateBlokey_whenUpdateBlokeyByBlokeyId_thenCallStateChangingMethod() {
        // given
        UUID id = UUID.randomUUID();
        BlokeyReqUpdateDto dto = new BlokeyReqUpdateDto("newNickname", "newBio");
        Blokey blokey = mock(Blokey.class);

        when(blokeyRepository.findById(id)).thenReturn(Optional.of(blokey));

        // when
        blokeyService.updateBlokeyByBlokeyId(id, dto);

        // then
        verify(blokeyRepository).findById(id);
        verify(blokey).updateNickname("newNickname");
        verify(blokey).updateBio("newBio");
    }

    @Test
    @DisplayName("존재하는 Blokey 삭제 시 delete 메서드 호출되어야 한다.")
    void deleteBlokey_whenDeleteBlokeyByBlokeyId_thenCallRepositoryDeleteMethod() {
        // given
        UUID id = UUID.randomUUID();
        Blokey blokey = mock(Blokey.class);

        when(blokeyRepository.findById(id)).thenReturn(Optional.of(blokey));

        // when
        blokeyService.deleteBlokeyByBlokeyId(id);

        // then
        verify(blokeyRepository).delete(blokey);
    }
}