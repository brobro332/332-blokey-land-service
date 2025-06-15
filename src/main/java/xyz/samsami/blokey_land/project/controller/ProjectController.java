package xyz.samsami.blokey_land.project.controller;

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
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.service.ProjectService;

@RestController
@RequiredArgsConstructor
public class ProjectController implements ProjectApi {
    private final ProjectService service;

    @Override
    public CommonRespDto<Void> createProject(@RequestBody ProjectReqCreateDto dto) {
        service.createProject(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 등록 완료", null);
    }

    @Override
    public CommonRespDto<Page<ProjectRespDto>> readProjects(
        @PageableDefault(
            sort = "id", direction = Sort.Direction.DESC
        ) Pageable pageable
    ) {
        Page<ProjectRespDto> page = service.readProjects(pageable);
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

    @Override
    public CommonRespDto<Void> restoreProjectByProjectId(@PathVariable Long projectId) {
        service.restoreProjectByProjectId(projectId);
        return CommonRespDto.of(ResultType.SUCCESS, "프로젝트 복구 완료", null);
    }
}