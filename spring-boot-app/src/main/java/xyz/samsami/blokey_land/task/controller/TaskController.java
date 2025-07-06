package xyz.samsami.blokey_land.task.controller;

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
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskReqUpdateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.service.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskController implements TaskApi {
    private final TaskService service;

    @Override
    public CommonRespDto<TaskRespDto> createTask(@RequestBody TaskReqCreateDto dto) {
        TaskRespDto response = service.createTask(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 등록 완료", response);
    }

    @Override
    public CommonRespDto<List<TaskRespDto>> readAllTasksByProjectId(
        @PathVariable Long projectId
    ) {
        List<TaskRespDto> list = service.readAllTasksByProjectId(projectId);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 전체 목록 조회 완료", list);
    }

    @Override
    public CommonRespDto<Page<TaskRespDto>> readTasksByProjectId(
        @PathVariable Long projectId,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<TaskRespDto> page = service.readTasksByProjectId(projectId, pageable);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 목록 조회 완료", page);
    }

    @Override
    public CommonRespDto<TaskRespDto> readTaskByTaskId(@PathVariable Long taskId) {
        TaskRespDto dto = service.readTaskByTaskId(taskId);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 정보 조회 완료", dto);
    }

    @Override
    public CommonRespDto<Void> updateTaskByTaskId(@PathVariable Long taskId, @RequestBody TaskReqUpdateDto dto) {
        service.updateTaskByTaskId(taskId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 정보 수정 완료", null);
    }

    @Override
    public CommonRespDto<Void> deleteTaskByTaskId(@PathVariable Long taskId) {
        service.deleteTaskByTaskId(taskId);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 삭제 완료", null);
    }
}