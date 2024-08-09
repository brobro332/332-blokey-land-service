package kr.co.co_working.task.repository;

import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;
import kr.co.co_working.task.repository.entity.Task;

import java.util.List;

public interface TaskDslRepository {
    List<TaskResponseDto> readTaskList(TaskRequestDto.READ dto);
    List<Task> selectTaskListByProject(Long id);
}
