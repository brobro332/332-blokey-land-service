package xyz.samsami.blokey_land.user.mapper;

import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.dto.UserReqCreateDto;
import xyz.samsami.blokey_land.user.dto.UserRespDto;

import java.util.UUID;

public class UserMapper {
    public static UserRespDto toRespDto(User user) {
        return UserRespDto.builder()
            .id(user.getId())
            .nickname(user.getNickname())
            .bio(user.getBio())
            .build();
    }

    public static User toEntity(UserReqCreateDto dto, UUID id) {
        return User.builder()
            .id(id)
            .nickname(dto.getNickname())
            .bio(dto.getBio())
            .build();
    }
}