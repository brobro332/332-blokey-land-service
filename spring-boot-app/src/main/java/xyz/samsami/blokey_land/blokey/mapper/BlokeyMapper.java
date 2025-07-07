package xyz.samsami.blokey_land.blokey.mapper;

import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;

public class BlokeyMapper {
    public static BlokeyRespDto toRespDto(Blokey blokey) {
        return BlokeyRespDto.builder()
            .id(blokey.getId())
            .nickname(blokey.getNickname())
            .bio(blokey.getBio())
            .build();
    }

    public static Blokey toEntity(BlokeyReqCreateDto dto) {
        return Blokey.builder()
            .id(dto.getId())
            .nickname(dto.getNickname())
            .bio(dto.getBio())
            .build();
    }
}