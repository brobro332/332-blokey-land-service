package xyz.samsami.blokey_land.milestone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqReadDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.milestone.service.MilestoneService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MilestoneController implements MilestoneApi {
    private final MilestoneService service;

    @Override
    public CommonRespDto<Void> createMilestone(@PathVariable Long projectId, @RequestBody MilestoneReqCreateDto dto) {
        service.createMilestone(projectId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 등록 완료", null);
    }

    @Override
    public CommonRespDto<List<MilestoneRespDto>> readMilestones(
        @ModelAttribute MilestoneReqReadDto dto
    ) {
        List<MilestoneRespDto> list = service.readMilestones(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 목록 조회 완료", list);
    }

    @Override
    public CommonRespDto<List<MilestoneRespDto>> readMilestonesByProjectId(
        @PathVariable Long projectId
    ) {
        List<MilestoneRespDto> list = service.readMilestonesByProjectId(projectId);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 목록 조회 완료", list);
    }

    @Override
    public CommonRespDto<Void> updateMilestoneByMilestoneId(@PathVariable Long milestoneId, @RequestBody MilestoneReqUpdateDto dto) {
        service.updateMilestoneByMilestoneId(milestoneId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 정보 수정 완료", null);
    }

    @Override
    public CommonRespDto<Void> deleteMilestoneByMilestoneId(@PathVariable Long milestoneId) {
        service.deleteMilestoneByMilestoneId(milestoneId);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 삭제 완료", null);
    }

    @Override
    public CommonRespDto<Void> setMilestoneToTask(
        @PathVariable Long taskId,
        @RequestParam(required = false) Long milestoneId
    ) {
        service.setMilestoneToTask(taskId, milestoneId);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 설정 완료", null);
    }
}