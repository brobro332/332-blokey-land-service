package xyz.samsami.blokey_land.milestone.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;

@RequestMapping("/api")
@Tag(name = "Milestone API", description = "마일스톤 관련 API")
public interface MilestoneApi {

    @Operation(summary = "마일스톤 생성", description = "프로젝트에 마일스톤을 등록합니다.")
    @PostMapping("/projects/{projectId}/milestones")
    CommonRespDto<Void> createMilestone(
        @PathVariable Long projectId,
        @RequestBody MilestoneReqCreateDto dto
    );

    @Operation(summary = "마일스톤 목록 조회", description = "프로젝트 도메인에서 마일스톤 목록을 조회합니다.")
    @GetMapping("/projects/{projectId}/milestones")
    CommonRespDto<Page<MilestoneRespDto>> readMilestones(
        @PathVariable Long projectId,
        @Parameter(hidden = true) Pageable pageable
    );

    @Operation(summary = "마일스톤 정보 수정", description = "마일스톤 정보를 수정합니다.")
    @PatchMapping("/milestones/{milestoneId}")
    CommonRespDto<Void> updateMilestoneByMilestoneId(
        @PathVariable Long milestoneId,
        @RequestBody MilestoneReqUpdateDto dto
    );

    @Operation(summary = "마일스톤 삭제", description = "마일스톤을 삭제합니다.")
    @DeleteMapping("/milestones/{milestoneId}")
    CommonRespDto<Void> deleteMilestoneByMilestoneId(@PathVariable Long milestoneId);

    @Operation(summary = "작업에 마일스톤 설정", description = "작업에 마일스톤을 설정하거나 해제합니다.")
    @PatchMapping("/tasks/{taskId}/milestone")
    CommonRespDto<Void> setMilestoneToTask(
        @PathVariable Long taskId,
        @RequestParam(required = false) Long milestoneId
    );
}