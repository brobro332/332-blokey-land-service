package xyz.samsami.blokey_land.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectReqCreateDto;
import xyz.samsami.blokey_land.project.dto.ProjectReqUpdateDto;
import xyz.samsami.blokey_land.project.dto.ProjectRespDto;
import xyz.samsami.blokey_land.project.mapper.ProjectMapper;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.service.UserService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final UserService userService;
    private final ProjectRepository repository;

    @Transactional
    public void createProject(ProjectReqCreateDto dto) {
        User user = userService.findUserByUserId(dto.getOwnerId());

        if (user != null) {
            Project project = ProjectMapper.toEntity(dto);

            repository.save(project);
        }
    }

    public Page<ProjectRespDto> readProjects(Pageable pageable) {
        return repository.findPageAll(pageable)
            .map(ProjectMapper::toRespDto);
    }

    public ProjectRespDto readProjectByProjectId(Long projectId) {
        Project project = findProjectByProjectId(projectId);

        return ProjectMapper.toRespDto(project);
    }

    @Transactional
    public void updateProjectByProjectId(Long projectId, ProjectReqUpdateDto dto) {
        Project project = findProjectByProjectId(projectId);

        project.updateTitle(dto.getTitle());
        project.updateDescription(dto.getDescription());
        project.updateOwnerId(dto.getOwnerId());
        project.updateStartDate(dto.getStartDate());
        project.updateEndDate(dto.getEndDate());
    }

    @Transactional
    public void deleteProjectByProjectId(Long projectId) {
        Project project = findProjectByProjectId(projectId);

        repository.delete(project);
    }

    public Project findProjectByProjectId(Long projectId) {
        return repository.findOptionalById(projectId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}