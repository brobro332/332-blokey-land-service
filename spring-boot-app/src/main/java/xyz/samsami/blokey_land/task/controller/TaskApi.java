package xyz.samsami.blokey_land.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import xyz.samsami.blokey_land.common.dto.CommonRespDto;
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskReqUpdateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;

import java.util.List;

@RequestMapping("/api")
@Tag(name = "Task API", description = "태스크 관련 API")
public interface TaskApi {
    @Operation(summary = "태스크 생성", description = "태스크를 생성합니다.")
    @PostMapping("/tasks")
    CommonRespDto<TaskRespDto> createTask(@RequestBody TaskReqCreateDto dto);

    @Operation(summary = "태스크 전체 목록 조회", description = "태스크 전체 목록을 조회합니다.")
    @GetMapping("projects/{projectId}/tasks/all")
    CommonRespDto<List<TaskRespDto>> readAllTasksByProjectId(
        @PathVariable Long projectId
    );

    @Operation(summary = "태스크 목록 조회", description = "태스크 목록을 조회합니다.")
    @GetMapping("projects/{projectId}/tasks")
    CommonRespDto<Page<TaskRespDto>> readTasksByProjectId(
        @PathVariable Long projectId,
        @Parameter(hidden = true)
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
        Pageable pageable
    );

    @Operation(summary = "태스크 단건 조회", description = "태스크 상세 정보를 조회합니다.")
    @GetMapping("/tasks/{taskId}")
    CommonRespDto<TaskRespDto> readTaskByTaskId(@PathVariable Long taskId);

    @Operation(summary = "태스크 수정", description = "태스크 정보를 수정합니다.")
    @PatchMapping("/tasks/{taskId}")
    CommonRespDto<Void> updateTaskByTaskId(@PathVariable Long taskId, @RequestBody TaskReqUpdateDto dto);

    @Operation(summary = "태스크 삭제", description = "태스크를 삭제합니다.")
    @DeleteMapping("/tasks/{taskId}")
    CommonRespDto<Void> deleteTaskByTaskId(@PathVariable Long taskId);
}