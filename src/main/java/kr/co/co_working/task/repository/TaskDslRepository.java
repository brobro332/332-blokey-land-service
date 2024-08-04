package kr.co.co_working.task.repository;

import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;

import java.util.List;

public interface TaskDslRepository {
    List<TaskResponseDto> selectTaskList(TaskRequestDto.READ dto);
}
