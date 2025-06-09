package xyz.samsami.blokey_land.user.service;

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
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.dto.UserReqCreateDto;
import xyz.samsami.blokey_land.user.dto.UserReqUpdateDto;
import xyz.samsami.blokey_land.user.dto.UserRespDto;
import xyz.samsami.blokey_land.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserHelperService helperService;

    @Mock
    private UserRepository repository;

    @Test
    void createUser_validInput_true() {
        // given
        UUID userId = UUID.randomUUID();
        UserReqCreateDto dto = UserReqCreateDto.builder()
            .nickname("닉네임")
            .bio("자기소개입니다.")
            .build();

        when(helperService.createUserOnAuthenticationServer(dto)).thenReturn(userId);

        // when
        service.createUser(dto);

        // then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(captor.capture());
        User capturedUser = captor.getValue();

        assertEquals(userId, capturedUser.getId());
        assertEquals(dto.getNickname(), capturedUser.getNickname());
        assertEquals(dto.getBio(), capturedUser.getBio());
    }

    @Test
    void readUsers_validInput_true() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        User user1 = new User(UUID.randomUUID(), "짱구", "감자머리입니다.");
        User user2 = new User(UUID.randomUUID(), "철수", "삐침머리입니다.");

        List<User> users = List.of(user1, user2);
        Page<User> page = new PageImpl<>(users, pageable, users.size());

        when(repository.findPageAll(pageable)).thenReturn(page);

        // when
        Page<UserRespDto> result = service.readUsers(pageable);

        // then
        assertEquals(users.size(), result.getNumberOfElements());

        for (int i = 0; i < users.size(); i++) {
            UserRespDto dto = result.getContent().get(i);
            User user = users.get(i);

            assertEquals(user.getNickname(), dto.getNickname());
        }

        verify(repository, times(1)).findPageAll(pageable);
    }

    @Test
    void readUserByUserId_validInput_true() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "짱구", "감자머리입니다.");

        when(repository.findOptionalById(userId)).thenReturn(Optional.of(user));

        // when
        UserRespDto result = service.readUserByUserId(userId);

        // then
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getNickname(), result.getNickname());
        assertEquals(user.getBio(), result.getBio());

        verify(repository, times(1)).findOptionalById(userId);
    }

    @Test
    void updateUserByUserId_onlyPasswordGiven_true() {
        // given
        UUID userId = UUID.randomUUID();
        UserReqUpdateDto dto = UserReqUpdateDto.builder()
            .password("password")
            .build();

        doNothing().when(helperService).updatePasswordOnAuthenticationServer(any(), any());

        // when
        service.updateUserByUserId(userId, dto);

        // then
        verify(helperService).updatePasswordOnAuthenticationServer(userId, dto);
        verify(repository, never()).findOptionalById(any());
    }

    @Test
    void updateUserByUserId_nicknameOrBioGiven_true() {
        // given
        UUID userId = UUID.randomUUID();
        UserReqUpdateDto dto = UserReqUpdateDto.builder()
            .nickname("닉네임")
            .bio("자기소개입니다.")
            .build();

        User user = mock(User.class);

        when(repository.findOptionalById(userId)).thenReturn(Optional.of(user));

        // when
        service.updateUserByUserId(userId, dto);

        // then
        verify(user).updateNickname("닉네임");
        verify(user).updateBio("자기소개입니다.");
        verify(helperService, never()).updatePasswordOnAuthenticationServer(any(), any());
    }

    @Test
    void updateUserByUserId_allFieldsEmpty_true() {
        // given
        UUID userId = UUID.randomUUID();
        UserReqUpdateDto dto = new UserReqUpdateDto();

        // when
        service.updateUserByUserId(userId, dto);

        // then
        verify(helperService, never()).updatePasswordOnAuthenticationServer(any(), any());
        verify(repository, never()).findOptionalById(any());
    }

    @Test
    void updateUserByUserId_userNotFound_false() {
        // given
        UUID userId = UUID.randomUUID();
        UserReqUpdateDto dto = UserReqUpdateDto.builder()
            .nickname("닉네임")
            .bio("자기소개입니다.")
            .build();

        when(repository.findOptionalById(userId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () ->
            service.updateUserByUserId(userId, dto)
        );

        assertEquals(ExceptionType.NOT_FOUND, e.getException());
    }

    @Test
    void deleteUserByUserId_validInput_true() {
        // given
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "짱구", "감자머리입니다.");

        doNothing().when(helperService).deleteUserOnAuthenticationServer(userId);
        when(repository.findOptionalById(userId)).thenReturn(Optional.of(user));

        // when
        service.deleteUserByUserId(userId);

        // then
        verify(helperService).deleteUserOnAuthenticationServer(userId);
        verify(repository).findOptionalById(userId);
        verify(repository).delete(user);
    }

    @Test
    void deleteUserByUserId_userNotFound_false() {
        // given
        UUID userId = UUID.randomUUID();

        doNothing().when(helperService).deleteUserOnAuthenticationServer(userId);
        when(repository.findOptionalById(userId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () -> {
            service.deleteUserByUserId(userId);
        });

        assertEquals(ExceptionType.NOT_FOUND, e.getException());

        verify(helperService).deleteUserOnAuthenticationServer(userId);
        verify(repository).findOptionalById(userId);
        verify(repository, never()).delete(any());
    }
}