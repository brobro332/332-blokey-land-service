package xyz.samsami.blokey_land.blokey.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqCreateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyReqUpdateDto;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BlokeyController implements BlokeyApi {
    private final BlokeyService service;

    @Override
    public CommonRespDto<Void> createBlokey(@RequestBody BlokeyReqCreateDto dto) {
        service.createBlokey(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 등록 완료", null);
    }

    @Override
    public CommonRespDto<Page<BlokeyRespDto>> readBlokeys(
            @RequestParam(required = false) Long excludeProjectId,
            @PageableDefault(
                sort = "nickname", direction = Sort.Direction.ASC
            ) Pageable pageable) {
        Page<BlokeyRespDto> page = service.readBlokeys(excludeProjectId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 목록 조회 완료", page);
    }

    @Override
    public CommonRespDto<BlokeyRespDto> readBlokeyByBlokeyId(@PathVariable UUID blokeyId) {
        BlokeyRespDto dto = service.readBlokeyByBlokeyId(blokeyId);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 정보 조회 완료", dto);
    }

    @Override
    public CommonRespDto<BlokeyRespDto> readBlokeyBySession(@RequestHeader("X-Account-Id") String blokeyId) {
        BlokeyRespDto dto = service.readBlokeyByBlokeyId(UUID.fromString(blokeyId));
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 정보 조회 완료", dto);
    }

    @Override
    public CommonRespDto<Void> updateBlokeyByBlokeyId(@PathVariable UUID blokeyId, @RequestBody BlokeyReqUpdateDto dto) {
        service.updateBlokeyByBlokeyId(blokeyId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 정보 수정 완료", null);
    }

    @Override
    public CommonRespDto<Void> deleteBlokeyByBlokeyId(@PathVariable UUID blokeyId) {
        service.deleteBlokeyByBlokeyId(blokeyId);
        return CommonRespDto.of(ResultType.SUCCESS, "사용자 삭제 완료", null);
    }
}