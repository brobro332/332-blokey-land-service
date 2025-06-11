package xyz.samsami.blokey_land.blokey.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqUpdateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlokeyServiceTest {
    @InjectMocks
    private BlokeyService service;

    @Mock
    private BlokeyHelperService helperService;

    @Mock
    private BlokeyRepository repository;

    @Test
    void createBlokey_validInput_true() {
        // given
        UUID blokeyId = UUID.randomUUID();
        BlokeyReqCreateDto dto = BlokeyReqCreateDto.builder()
            .nickname("닉네임")
            .bio("자기소개입니다.")
            .build();

        when(helperService.createBlokeyOnAuthenticationServer(dto)).thenReturn(blokeyId);

        // when
        service.createBlokey(dto);

        // then
        ArgumentCaptor<Blokey> captor = ArgumentCaptor.forClass(Blokey.class);
        verify(repository, times(1)).save(captor.capture());
        Blokey capturedBlokey = captor.getValue();

        assertEquals(blokeyId, capturedBlokey.getId());
        assertEquals(dto.getNickname(), capturedBlokey.getNickname());
        assertEquals(dto.getBio(), capturedBlokey.getBio());
    }

    @Test
    void readBlokeys_validInput_true() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Blokey blokey1 = new Blokey(UUID.randomUUID(), "짱구", "감자머리입니다.");
        Blokey blokey2 = new Blokey(UUID.randomUUID(), "철수", "삐침머리입니다.");

        List<Blokey> blokeys = List.of(blokey1, blokey2);
        Page<Blokey> page = new PageImpl<>(blokeys, pageable, blokeys.size());

        when(repository.findAll(pageable)).thenReturn(page);

        // when
        Page<BlokeyRespDto> result = service.readBlokeys(pageable);

        // then
        assertEquals(blokeys.size(), result.getNumberOfElements());

        for (int i = 0; i < blokeys.size(); i++) {
            BlokeyRespDto dto = result.getContent().get(i);
            Blokey blokey = blokeys.get(i);

            assertEquals(blokey.getNickname(), dto.getNickname());
        }

        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void readBlokeyByBlokeyId_validInput_true() {
        // given
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey = new Blokey(blokeyId, "짱구", "감자머리입니다.");

        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        BlokeyRespDto result = service.readBlokeyByBlokeyId(blokeyId);

        // then
        assertEquals(blokey.getId(), result.getId());
        assertEquals(blokey.getNickname(), result.getNickname());
        assertEquals(blokey.getBio(), result.getBio());

        verify(repository, times(1)).findById(blokeyId);
    }

    @Test
    void updateBlokeyByBlokeyId_onlyPasswordGiven_true() {
        // given
        UUID blokeyId = UUID.randomUUID();
        BlokeyReqUpdateDto dto = BlokeyReqUpdateDto.builder()
            .password("password")
            .build();

        doNothing().when(helperService).updatePasswordOnAuthenticationServer(any(), any());

        // when
        service.updateBlokeyByBlokeyId(blokeyId, dto);

        // then
        verify(helperService).updatePasswordOnAuthenticationServer(blokeyId, dto);
        verify(repository, never()).findById(any());
    }

    @Test
    void updateBlokeyByBlokeyId_nicknameOrBioGiven_true() {
        // given
        UUID blokeyId = UUID.randomUUID();
        BlokeyReqUpdateDto dto = BlokeyReqUpdateDto.builder()
            .nickname("닉네임")
            .bio("자기소개입니다.")
            .build();

        Blokey blokey = mock(Blokey.class);

        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        service.updateBlokeyByBlokeyId(blokeyId, dto);

        // then
        verify(blokey).updateNickname("닉네임");
        verify(blokey).updateBio("자기소개입니다.");
        verify(helperService, never()).updatePasswordOnAuthenticationServer(any(), any());
    }

    @Test
    void updateBlokeyByBlokeyId_allFieldsEmpty_true() {
        // given
        UUID blokeyId = UUID.randomUUID();
        BlokeyReqUpdateDto dto = new BlokeyReqUpdateDto();

        // when
        service.updateBlokeyByBlokeyId(blokeyId, dto);

        // then
        verify(helperService, never()).updatePasswordOnAuthenticationServer(any(), any());
        verify(repository, never()).findById(any());
    }

    @Test
    void updateBlokeyByBlokeyId_blokeyNotFound_false() {
        // given
        UUID blokeyId = UUID.randomUUID();
        BlokeyReqUpdateDto dto = BlokeyReqUpdateDto.builder()
            .nickname("닉네임")
            .bio("자기소개입니다.")
            .build();

        when(repository.findById(blokeyId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () ->
            service.updateBlokeyByBlokeyId(blokeyId, dto)
        );

        assertEquals(ExceptionType.NOT_FOUND, e.getException());
    }

    @Test
    void deleteBlokeyByBlokeyId_validInput_true() {
        // given
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey = new Blokey(blokeyId, "짱구", "감자머리입니다.");

        doNothing().when(helperService).deleteBlokeyOnAuthenticationServer(blokeyId);
        when(repository.findById(blokeyId)).thenReturn(Optional.of(blokey));

        // when
        service.deleteBlokeyByBlokeyId(blokeyId);

        // then
        verify(helperService).deleteBlokeyOnAuthenticationServer(blokeyId);
        verify(repository).findById(blokeyId);
        verify(repository).delete(blokey);
    }

    @Test
    void deleteBlokeyByBlokeyId_blokeyNotFound_false() {
        // given
        UUID blokeyId = UUID.randomUUID();

        doNothing().when(helperService).deleteBlokeyOnAuthenticationServer(blokeyId);
        when(repository.findById(blokeyId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () -> {
            service.deleteBlokeyByBlokeyId(blokeyId);
        });

        assertEquals(ExceptionType.NOT_FOUND, e.getException());

        verify(helperService).deleteBlokeyOnAuthenticationServer(blokeyId);
        verify(repository).findById(blokeyId);
        verify(repository, never()).delete(any());
    }
}