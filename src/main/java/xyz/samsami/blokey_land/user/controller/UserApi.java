package xyz.samsami.blokey_land.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.user.dto.UserReqCreateDto;
import xyz.samsami.blokey_land.user.dto.UserReqUpdateDto;
import xyz.samsami.blokey_land.user.dto.UserRespDto;

import java.util.UUID;

@RequestMapping("/api/users")
public interface UserApi {
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @PostMapping
    CommonRespDto<Void> createUser(@RequestBody UserReqCreateDto dto);

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회합니다.")
    @GetMapping
    CommonRespDto<Page<UserRespDto>> readUsers(
        @Parameter(hidden = true)
        @PageableDefault(sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable
    );

    @Operation(summary = "사용자 단건 조회", description = "사용자 상세 정보를 조회합니다.")
    @GetMapping("/{userId}")
    CommonRespDto<UserRespDto> readUserByUserId(@PathVariable UUID userId);

    @Operation(summary = "사용자 수정", description = "사용자 정보를 수정합니다.")
    @PatchMapping("/{userId}")
    CommonRespDto<Void> updateUserByUserId(@PathVariable UUID userId, @RequestBody UserReqUpdateDto dto);

    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @DeleteMapping("/{userId}")
    CommonRespDto<Void> deleteUserByUserId(@PathVariable UUID userId);
}