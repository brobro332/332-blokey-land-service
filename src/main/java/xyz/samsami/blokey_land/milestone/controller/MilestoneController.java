package xyz.samsami.blokey_land.milestone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.milestone.service.MilestoneService;

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
    public CommonRespDto<Page<MilestoneRespDto>> readMilestones(
        @PathVariable Long projectId,
        @PageableDefault(
            sort = "id", direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        Page<MilestoneRespDto> page = service.readMilestonesByProjectId(projectId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "마일스톤 목록 조회 완료", page);
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