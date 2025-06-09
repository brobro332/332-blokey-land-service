package xyz.samsami.blokey_land.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.util.StringUtil;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.dto.UserReqCreateDto;
import xyz.samsami.blokey_land.user.dto.UserReqUpdateDto;
import xyz.samsami.blokey_land.user.dto.UserRespDto;
import xyz.samsami.blokey_land.user.mapper.UserMapper;
import xyz.samsami.blokey_land.user.repository.UserRepository;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserHelperService helperService;
    private final UserRepository repository;

    @Transactional
    public void createUser(UserReqCreateDto dto) {
        UUID userId = helperService.createUserOnAuthenticationServer(dto);
        User user = UserMapper.toEntity(dto, userId);

        repository.save(user);
    }

    public Page<UserRespDto> readUsers(Pageable pageable) {
        return repository.findPageAll(pageable)
            .map(UserMapper::toRespDto);
    }

    public UserRespDto readUserByUserId(UUID userId) {
        User user = helperService.existsUserByUserId(userId);

        return UserMapper.toRespDto(user);
    }

    @Transactional
    public void updateUserByUserId(UUID userId, UserReqUpdateDto dto) {
        if (!StringUtil.isNullOrEmpty(dto.getPassword())) helperService.updatePasswordOnAuthenticationServer(userId, dto);

        if (StringUtil.anyNotNullOrEmpty(dto.getNickname(), dto.getBio())) {
            User user = helperService.existsUserByUserId(userId);

            user.updateNickname(dto.getNickname());
            user.updateBio(dto.getBio());
        }
    }

    @Transactional
    public void deleteUserByUserId(UUID userId) {
        helperService.deleteUserOnAuthenticationServer(userId);
        User user = helperService.existsUserByUserId(userId);

        repository.delete(user);
    }
}