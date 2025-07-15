package xyz.samsami.blokey_land.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.*;
import xyz.samsami.blokey_land.project.mapper.ProjectMapper;
import xyz.samsami.blokey_land.project.repository.ProjectDslRepository;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.mapper.TaskMapper;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final BlokeyService blokeyService;
    private final MemberService memberService;
    private final ProjectRepository repository;
    private final ProjectDslRepository dslRepository;

    @Transactional
    public void createProject(ProjectReqCreateDto dto, String blokeyId) {
        UUID id = UUID.fromString(blokeyId);
        Blokey blokey = blokeyService.findBlokeyByBlokeyId(id);
        if (blokey != null) {
            Project project = repository.save(ProjectMapper.toEntity(dto));
            memberService.createMember(project, blokey, RoleType.LEADER);
        }
    }

    public List<ProjectOnlyRespDto> readAllProjects(String blokeyId) {
        return repository.findProjectsWithRoleByBlokeyId(UUID.fromString(blokeyId));
    }

    public List<ProjectWithTaskResponseDto> readAllProjectsWithTasks(String blokeyId) {
        List<Project> projects = repository.findProjectsWithTasksByBlokeyId(UUID.fromString(blokeyId));

        return projects.stream()
            .map(project -> {
                List<TaskRespDto> taskRespDtoList = project.getTasks()
                    .stream()
                    .map(TaskMapper::toRespDto)
                    .toList();

                return ProjectMapper.toRespDtoWithTaskDtoList(project, taskRespDtoList);
            })
            .toList();
    }

    public Slice<ProjectOnlyRespDto> readProjectsSlice(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        return dslRepository.readProjectsSlice(dto, blokeyId, pageable);
    }

    public Page<ProjectOnlyRespDto> readProjectsPage(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        return dslRepository.readProjectsPage(dto, blokeyId, pageable);
    }

    public ProjectOnlyRespDto readProjectByProjectId(Long projectId) {
        return ProjectMapper.toRespDto(findProjectByProjectId(projectId));
    }

    @Transactional
    public void updateProjectByProjectId(Long projectId, ProjectReqUpdateDto dto) {
        Project project = findProjectByProjectId(projectId);

        project.updateTitle(dto.getTitle());
        project.updateDescription(dto.getDescription());
        project.updateImageUrl(dto.getImageUrl());
        project.updateStatus(dto.getStatus());
        project.updateIsPrivate(dto.getIsPrivate());
        project.updateEstimatedStartDate(dto.getEstimatedStartDate());
        project.updateEstimatedEndDate(dto.getEstimatedEndDate());
        project.updateActualStartDate(dto.getActualStartDate());
        project.updateActualEndDate(dto.getActualEndDate());
    }

    @Transactional
    public void softDeleteProjectByProjectId(Long projectId) {
        Project project = findProjectByProjectId(projectId);
        if (project != null) project.updateStatus(ProjectStatusType.DELETED);
    }

    public Project findProjectByProjectId(Long projectId) {
        return repository.findById(projectId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}