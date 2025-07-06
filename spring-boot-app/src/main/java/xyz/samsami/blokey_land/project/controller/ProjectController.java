package xyz.samsami.blokey_land.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqReadDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.service.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {
    private final ProjectService service;

    @Override
    public CommonRespDto<Void> createProject(
        @RequestBody ProjectReqCreateDto dto,
        @RequestHeader("X-Account-Id") String blokeyId
    ) {
        service.createProject(dto, blokeyId);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 등록 완료", null);
    }

    @Override
    public CommonRespDto<List<ProjectRespDto>> readAllProjects(
        @RequestHeader("X-Account-Id") String blokeyId
    ) {
        List<ProjectRespDto> list = service.readAllProjects(blokeyId);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 목록 조회 완료", list);
    }

    @Override
    public CommonRespDto<Slice<ProjectRespDto>> readProjectsSlice(
        @ModelAttribute ProjectReqReadDto dto,
        @RequestHeader("X-Account-Id") String blokeyId,
        @PageableDefault(
            sort = "id", direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        Slice<ProjectRespDto> slice = service.readProjectsSlice(dto, blokeyId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 목록 조회 완료", slice);
    }

    @Override
    public CommonRespDto<Page<ProjectRespDto>> readProjectsPage(
            @ModelAttribute ProjectReqReadDto dto,
            @RequestHeader("X-Account-Id") String blokeyId,
            @PageableDefault(
                sort = "id", direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<ProjectRespDto> page = service.readProjectsPage(dto, blokeyId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 목록 조회 완료", page);
    }

    @Override
    public CommonRespDto<ProjectRespDto> readProjectByProjectId(@PathVariable Long projectId) {
        ProjectRespDto dto = service.readProjectByProjectId(projectId);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 정보 조회 완료", dto);
    }

    @Override
    public CommonRespDto<Void> updateProjectByProjectId(@PathVariable Long projectId, @RequestBody ProjectReqUpdateDto dto) {
        service.updateProjectByProjectId(projectId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 정보 수정 완료", null);
    }

    @Override
    public CommonRespDto<Void> deleteProjectByProjectId(@PathVariable Long projectId) {
        service.softDeleteProjectByProjectId(projectId);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 삭제 완료", null);
    }
}