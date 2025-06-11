package xyz.samsami.blokey_land.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.user.dto.UserReqCreateDto;
import xyz.samsami.blokey_land.user.dto.UserReqUpdateDto;
import xyz.samsami.blokey_land.user.dto.UserRespDto;
import xyz.samsami.blokey_land.user.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {
    private final UserService service;

    @Override
    public CommonRespDto<Void> createUser(@RequestBody UserReqCreateDto dto) {
        service.createUser(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 등록 완료", null);
    }

    @Override
    public CommonRespDto<Page<UserRespDto>> readUsers(
            @PageableDefault(
                sort = "nickname", direction = Sort.Direction.ASC
            ) Pageable pageable) {
        Page<UserRespDto> page = service.readUsers(pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 목록 조회 완료", page);
    }

    @Override
    public CommonRespDto<UserRespDto> readUserByUserId(@PathVariable UUID userId) {
        UserRespDto dto = service.readUserByUserId(userId);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 정보 조회 완료", dto);
    }

    @Override
    public CommonRespDto<Void> updateUserByUserId(@PathVariable UUID userId, @RequestBody UserReqUpdateDto dto) {
        service.updateUserByUserId(userId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 정보 수정 완료", null);
    }

    @Override
    public CommonRespDto<Void> deleteUserByUserId(@PathVariable UUID userId) {
        service.deleteUserByUserId(userId);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 삭제 완료", null);
    }
}