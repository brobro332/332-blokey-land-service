package xyz.samsami.blokey_land.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.mapper.ProjectMapper;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final BlokeyService blokeyService;
    private final MemberService memberService;
    private final ProjectRepository repository;

    @Transactional
    public void createProject(ProjectReqCreateDto dto) {
        Blokey blokey = blokeyService.findBlokeyByBlokeyId(dto.getOwnerId());
        if (blokey != null) {
            Project project = repository.save(ProjectMapper.toEntity(dto));
            memberService.createMember(project, blokey);
        }
    }

    public Page<ProjectRespDto> readProjects(Pageable pageable) {
        return repository.findAll(pageable)
            .map(ProjectMapper::toRespDto);
    }

    public ProjectRespDto readProjectByProjectId(Long projectId) {
        return ProjectMapper.toRespDto(findProjectByProjectId(projectId));
    }

    @Transactional
    public void updateProjectByProjectId(Long projectId, ProjectReqUpdateDto dto) {
        Project project = findProjectByProjectId(projectId);

        project.updateTitle(dto.getTitle());
        project.updateDescription(dto.getDescription());
        project.updateOwnerId(dto.getOwnerId());
        project.updateEstimatedStartDate(dto.getEstimatedStartDate());
        project.updateEstimatedEndDate(dto.getEstimatedEndDate());
        project.updateActualStartDate(dto.getActualStartDate());
        project.updateActualEndDate(dto.getActualEndDate());
    }

    @Transactional
    public void softDeleteProjectByProjectId(Long projectId) {
        Project project = findProjectByProjectId(projectId);
        if (project != null) project.updateDeleted(true);
    }

    @Transactional
    public void restoreProjectByProjectId(Long projectId) {
        Project project = findProjectByProjectId(projectId);
        if (project != null) project.updateDeleted(false);
    }

    public Project findProjectByProjectId(Long projectId) {
        return repository.findById(projectId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}