package xyz.samsami.blokey_land.milestone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqReadDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.milestone.mapper.MilestoneMapper;
import xyz.samsami.blokey_land.milestone.repository.MilestoneDslRepository;
import xyz.samsami.blokey_land.milestone.repository.MilestoneRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MilestoneService {
    private final ProjectService projectService;
    private final MilestoneRepository repository;
    private final MilestoneDslRepository dslRepository;

    @Transactional
    public void createMilestone(Long projectId, MilestoneReqCreateDto dto) {
        Project project = projectService.findProjectByProjectId(projectId);
        if (project != null) repository.save(MilestoneMapper.toEntity(dto, project));
    }

    public List<MilestoneRespDto> readMilestones(MilestoneReqReadDto dto) {
        return dslRepository.readMilestones(dto);
    }

    public List<MilestoneRespDto> readMilestonesByProjectId(Long projectId) {
        return repository.findDtoByProject(projectId);
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
        }
    }

    public Milestone findMilestoneByMilestoneId(Long milestoneId) {
        return repository.findById(milestoneId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}