package kr.co.co_working.task.controller.api;

import kr.co.co_working.common.dto.ResponseDto;
import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TaskApiController {
    private final TaskService service;

    /**
     * createTask : Task 등록
     * @param dto
     * @return
     * @throws Exception
     */
    @PostMapping("/api/v1/task")
    public ResponseDto<?> createTask(@RequestBody TaskRequestDto.CREATE dto) throws Exception {
        service.createTask(dto);

        return ResponseDto.ofSuccess("작업 등록에 성공했습니다.");
    }

    /**
     * selectTask : Task 조회
     * @param dto
     * @return
     * @throws Exception
     */
    @GetMapping("/api/v1/task")
    public ResponseDto<?> selectTaskList(@RequestBody TaskRequestDto.READ dto) throws Exception {
        return ResponseDto.ofSuccess("작업 조회에 성공했습니다.", service.selectTaskList(dto));
    }

    /**
     * updateTask : Task 수정
     * @param id
     * @param dto
     * @return
     * @throws Exception
     */
    @PutMapping("/api/v1/task/{task_id}")
    public ResponseDto<?> updateTask(@PathVariable(name = "task_id") Long id,
                                     @RequestBody TaskRequestDto.UPDATE dto) throws Exception {
        service.updateTask(id, dto);

        return ResponseDto.ofSuccess("작업 수정에 성공했습니다.");
    }

    /**
     * deleteTask : Task 삭제
     * @param id
     * @return
     * @throws Exception
     */
    @DeleteMapping("/api/v1/task/{task_id}")
    public ResponseDto<?> deleteTask(@PathVariable(name = "task_id") Long id) throws Exception {
        service.deleteTask(id);

        return ResponseDto.ofSuccess("작업 삭제에 성공했습니다.");
    }
}