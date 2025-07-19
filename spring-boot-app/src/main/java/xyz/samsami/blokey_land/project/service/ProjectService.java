package xyz.samsami.blokey_land.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.service.ProjectSkillService;
import xyz.samsami.blokey_land.skill.service.SkillService;
import xyz.samsami.blokey_land.skill.service.helper.SkillAttachHelper;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.mapper.TaskMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final BlokeyService blokeyService;
    private final MemberService memberService;
    private final SkillService skillService;
    private final ProjectSkillService projectSkillService;
    private final ProjectRepository repository;
    private final ProjectDslRepository dslRepository;
    private final SkillAttachHelper skillAttachHelper;

    @Transactional
    public void createProject(ProjectReqCreateDto dto, String blokeyId) {
        UUID id = UUID.fromString(blokeyId);
        Blokey blokey = blokeyService.findBlokeyByBlokeyId(id);

        if (blokey == null) throw new CommonException(ExceptionType.NOT_FOUND, null);

        Project project = repository.save(ProjectMapper.toEntity(dto));
        memberService.createMember(project, blokey, RoleType.LEADER);

        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            for (Long skillId : dto.getSkills()) {
                addSkillToProject(project, skillId);
            }
        }
    }

    public List<ProjectOnlyRespDto> readAllProjects(String blokeyId) {
        List<ProjectOnlyRespDto> list = repository.findProjectsWithRoleByBlokeyId(UUID.fromString(blokeyId));
        return skillAttachHelper.attachSkills(list);
    }

    public List<ProjectWithTaskRespDto> readAllProjectsWithTasks(String blokeyId) {
        List<Project> projects = repository.findProjectsWithTasksByBlokeyId(UUID.fromString(blokeyId));

        List<ProjectWithTaskRespDto> dtoList = projects.stream()
            .map(project -> {
                List<TaskRespDto> taskRespDtoList = project.getTasks().stream()
                    .map(TaskMapper::toRespDto)
                    .toList();

                return ProjectMapper.toRespDtoWithTaskDtoList(project, taskRespDtoList);
            })
            .toList();

        return skillAttachHelper.attachSkills(dtoList);
    }

    public Slice<ProjectOnlyRespDto> readProjectsSlice(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        Slice<ProjectOnlyRespDto> slice = dslRepository.readProjectsSlice(dto, blokeyId, pageable);
        List<ProjectOnlyRespDto> modifiedContent = skillAttachHelper.attachSkills(slice.getContent());
        return new SliceImpl<>(modifiedContent, pageable, slice.hasNext());
    }

    public Page<ProjectOnlyRespDto> readProjectsPage(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        Page<ProjectOnlyRespDto> page = dslRepository.readProjectsPage(dto, blokeyId, pageable);
        List<ProjectOnlyRespDto> modifiedContent = skillAttachHelper.attachSkills(page.getContent());
        return new PageImpl<>(modifiedContent, pageable, page.getTotalElements());
    }

    public ProjectOnlyRespDto readProjectByProjectId(Long projectId) {
        ProjectOnlyRespDto dto = ProjectMapper.toRespDto(findProjectByProjectId(projectId));
        List<ProjectOnlyRespDto> result = skillAttachHelper.attachSkills(List.of(dto));
        return result.getFirst();
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

        if (dto.getSkills() != null) {
            Set<Long> newSkillIds = new HashSet<>(dto.getSkills());
            Set<Long> existingSkillIds = project.getSkills().stream()
                .map(ps -> ps.getSkill().getId())
                .collect(Collectors.toSet());

            for (Long skillId : newSkillIds) {
                if (!existingSkillIds.contains(skillId)) addSkillToProject(project, skillId);
            }

            for (Long skillId : existingSkillIds) {
                if (!newSkillIds.contains(skillId)) removeSkillFromProject(project, skillId);
            }
        }
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

    @Transactional
    public void addSkillToProject(Project project, Long skillId) {
        Skill skill = skillService.findSkillBySkillId(skillId);

        projectSkillService.create(project, skill);
    }

    @Transactional
    public void removeSkillFromProject(Project project, Long skillId) {
        Skill skill = skillService.findSkillBySkillId(skillId);

        projectSkillService.delete(project, skill);
    }
}