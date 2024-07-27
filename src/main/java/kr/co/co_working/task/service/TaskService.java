package kr.co.co_working.service;

import kr.co.co_working.repository.entity.Task;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    public void createTask() throws Exception {
        try {
            Task task = Task.builder()
                    .name("task A")
                    .type("type A")
                    .description("desc A")
                    .build();



        } catch (Exception e) {
            throw new Exception("작업 등록 시 예외 발생");
        }
    }
}
