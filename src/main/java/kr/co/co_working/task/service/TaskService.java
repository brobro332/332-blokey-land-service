package kr.co.co_working.task.service;

import kr.co.co_working.task.repository.entity.Task;
import kr.co.co_working.task.repository.entity.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    public void createTask() throws Exception {
        try {
            Task task = Task.builder()
                    .name("task A")
                    .type("type A")
                    .description("desc A")
                    .build();

            repository.save(task);
        } catch (Exception e) {
            throw new Exception("작업 등록 시 예외 발생");
        }
    }
}
