package xyz.samsami.blokey_land.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskReqUpdateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.mapper.TaskMapper;
import xyz.samsami.blokey_land.task.repository.TaskRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {
    private final ProjectService projectService;
    private final TaskRepository repository;

    @Transactional
    public void createTask(TaskReqCreateDto dto) {
        Project project = projectService.findProjectByProjectId(dto.getProjectId());
        if (project != null) repository.save(TaskMapper.toEntity(dto, project));
    }

    public TaskRespDto readTaskByTaskId(Long taskId) {
        return TaskMapper.toRespDto(findTaskByTaskId(taskId));
    }

    @Transactional
    public void updateTaskByTaskId(Long taskId, TaskReqUpdateDto dto) {
        Task task = findTaskByTaskId(taskId);

        task.updateTitle(dto.getTitle());
        task.updateDescription(dto.getDescription());
        task.updateAssignee(dto.getAssignee());
        task.updateProgress(dto.getProgress());
        task.updateStatus(dto.getStatus());
        task.updatePriority(dto.getPriority());
        task.updateEstimatedStartDate(dto.getEstimatedStartDate());
        task.updateEstimatedEndDate(dto.getEstimatedEndDate());
        task.updateActualStartDate(dto.getActualStartDate());
        task.updateActualEndDate(dto.getActualEndDate());
    }

    @Transactional
    public void deleteTaskByTaskId(Long taskId) {
        repository.delete(findTaskByTaskId(taskId));
    }

    @Transactional
    public void clearMilestoneFromTasks(Milestone milestone) {
        repository.clearMilestoneFromTasks(milestone);
    }

    public Task findTaskByTaskId(Long taskId) {
        return repository.findById(taskId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}