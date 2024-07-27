package kr.co.co_working.task.controller.api;

import kr.co.co_working.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskApiController {
    private TaskService service;

    @PostMapping("/api/v1/task")
    public ResponseEntity<String> createTask() throws Exception {
        service.createTask();

        return ResponseEntity.ok("작업 등록에 성공했습니다.");
    }
}
