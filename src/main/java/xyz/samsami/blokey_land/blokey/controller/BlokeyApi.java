package xyz.samsami.blokey_land.blokey.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqUpdateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;

import java.util.UUID;

@RequestMapping("/api/blokeys")
public interface BlokeyApi {
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @PostMapping
    CommonRespDto<Void> createBlokey(@RequestBody BlokeyReqCreateDto dto);

    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회합니다.")
    @GetMapping
    CommonRespDto<Page<BlokeyRespDto>> readBlokeys(
        @Parameter(hidden = true)
        @PageableDefault(sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable
    );

    @Operation(summary = "사용자 단건 조회", description = "사용자 상세 정보를 조회합니다.")
    @GetMapping("/{blokeyId}")
    CommonRespDto<BlokeyRespDto> readBlokeyByBlokeyId(@PathVariable UUID blokeyId);

    @Operation(summary = "사용자 수정", description = "사용자 정보를 수정합니다.")
    @PatchMapping("/{blokeyId}")
    CommonRespDto<Void> updateBlokeyByBlokeyId(@PathVariable UUID blokeyId, @RequestBody BlokeyReqUpdateDto dto);

    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @DeleteMapping("/{blokeyId}")
    CommonRespDto<Void> deleteBlokeyByBlokeyId(@PathVariable UUID blokeyId);
}