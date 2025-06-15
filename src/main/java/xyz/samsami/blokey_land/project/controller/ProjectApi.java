package xyz.samsami.blokey_land.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;

@RequestMapping("/api/projects")
@Tag(name = "Project API", description = "프로젝트 관련 API")
public interface ProjectApi {
    @Operation(summary = "프로젝트 생성", description = "프로젝트를 생성합니다.")
    @PostMapping
    CommonRespDto<Void> createProject(@RequestBody ProjectReqCreateDto dto);

    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록을 조회합니다.")
    @GetMapping
    CommonRespDto<Page<ProjectRespDto>> readProjects(
        @Parameter(hidden = true)
        @PageableDefault(sort = "projectId", direction = Sort.Direction.DESC)
        Pageable pageable
    );

    @Operation(summary = "프로젝트 단건 조회", description = "프로젝트 상세 정보를 조회합니다.")
    @GetMapping("/{projectId}")
    CommonRespDto<ProjectRespDto> readProjectByProjectId(@PathVariable Long projectId);

    @Operation(summary = "프로젝트 수정", description = "프로젝트 정보를 수정합니다.")
    @PatchMapping("/{projectId}")
    CommonRespDto<Void> updateProjectByProjectId(@PathVariable Long projectId, @RequestBody ProjectReqUpdateDto dto);

    @Operation(summary = "프로젝트 삭제", description = "프로젝트를 임시 삭제합니다.")
    @DeleteMapping("/{projectId}")
    CommonRespDto<Void> deleteProjectByProjectId(@PathVariable Long projectId);

    @Operation(summary = "프로젝트 복구", description = "임시 삭제된 프로젝트를 복구합니다.")
    @PatchMapping("/{projectId}:restore")
    CommonRespDto<Void> restoreProjectByProjectId(@PathVariable Long projectId);
}