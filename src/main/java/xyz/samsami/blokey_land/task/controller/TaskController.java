package xyz.samsami.blokey_land.task.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.common.type.ResultType;
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskReqUpdateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.service.TaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    @PostMapping
    public CommonRespDto<Void> createTask(@RequestBody TaskReqCreateDto dto) {
        service.createTask(dto);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 등록 완료", null);
    }

    @GetMapping("/{taskId}")
    public CommonRespDto<TaskRespDto> readTaskByTaskId(@PathVariable Long taskId) {
        TaskRespDto dto = service.readTaskByTaskId(taskId);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 정보 조회 완료", dto);
    }

    @PatchMapping("/{taskId}")
    public CommonRespDto<Void> updateTaskByTaskId(@PathVariable Long taskId, @RequestBody TaskReqUpdateDto dto) {
        service.updateTaskByTaskId(taskId, dto);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 정보 수정 완료", null);
    }

    @DeleteMapping("/{taskId}")
    public CommonRespDto<Void> deleteTaskByTaskId(@PathVariable Long taskId) {
        service.deleteTaskByTaskId(taskId);
        return CommonRespDto.of(ResultType.SUCCESS, "태스크 삭제 완료", null);
    }
}