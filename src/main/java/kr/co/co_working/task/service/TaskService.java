package kr.co.co_working.task.service;

import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.repository.entity.Task;
import kr.co.co_working.task.repository.entity.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

    /**
     * createTask : Task 등록
     * @param dto
     * @throws Exception
     */
    public void createTask(TaskRequestDto.CREATE dto) throws Exception {
        try {
            // 1. Task 빌드
            Task task = Task.builder()
                    .name(dto.getName())
                    .type(dto.getType())
                    .description(dto.getDescription())
                    .build();

            // 2. 등록
            repository.save(task);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * updateTask : Task 수정
     * @param id
     * @param dto
     * @throws Exception
     */
    @Transactional
    public void updateTask(Long id, TaskRequestDto.UPDATE dto) throws Exception {
        try {
            // 1. ID에 해당하는 업무 조회
            Optional<Task> selectedTask = repository.findById(id);

            // 2. 부재 시 예외 처리
            if (selectedTask.isEmpty()) {
                throw new Exception("수정하려는 업무가 존재하지 않습니다.");
            }

            // 3. 존재 시 수정 처리
            selectedTask.get()
                    .updateTask(dto.getName(), dto.getType(), dto.getDescription());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * deleteTask : Task 삭제
     * @param id
     * @throws Exception
     */
    @Transactional
    public void deleteTask(Long id) throws Exception {
        try {
            // 1. ID에 해당하는 업무 조회
            Optional<Task> selectedTask = repository.findById(id);

            // 2. 부재 시 예외 처리
            if (selectedTask.isEmpty()) {
                throw new Exception("삭제하려는 업무가 존재하지 않습니다.");
            }

            // 3. 존재 시 삭제 처리
            repository.delete(selectedTask.get());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}