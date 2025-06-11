package xyz.samsami.blokey_land.milestone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.milestone.mapper.MilestoneMapper;
import xyz.samsami.blokey_land.milestone.repository.MilestoneRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.service.TaskService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MilestoneService {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final MilestoneRepository repository;

    @Transactional
    public void createMilestone(Long projectId, MilestoneReqCreateDto dto) {
        Project project = projectService.findProjectByProjectId(projectId);
        if (project != null) repository.save(MilestoneMapper.toEntity(dto, project));
    }

    public Page<MilestoneRespDto> readMilestonesByProjectId(Long projectId, Pageable pageable) {
        return repository.findByProjectId(projectId, pageable);
    }

    @Transactional
    public void updateMilestoneByMilestoneId(Long milestoneId, MilestoneReqUpdateDto dto) {
        Milestone milestone = findMilestoneByMilestoneId(milestoneId);

        if (milestone != null) {
            milestone.updateTitle(dto.getTitle());
            milestone.updateDescription(dto.getDescription());
            milestone.updateDueDate(dto.getDueDate());
        }
    }

    @Transactional
    public void deleteMilestoneByMilestoneId(Long milestoneId) {
        Milestone milestone = findMilestoneByMilestoneId(milestoneId);
        if (milestone != null) {
            repository.delete(milestone);
            taskService.clearMilestoneFromTasks(milestone);
        }
    }

    @Transactional
    public void setMilestoneToTask(Long taskId, Long milestoneId) {
        Milestone milestone = repository.findById(milestoneId).orElse(null);
        Task task = taskService.findTaskByTaskId(taskId);
        if (task != null) task.updateMilestone(milestone);
    }

    public Milestone findMilestoneByMilestoneId(Long milestoneId) {
        return repository.findById(milestoneId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}