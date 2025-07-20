package xyz.samsami.blokey_land.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.service.DisciplineService;
import xyz.samsami.blokey_land.discipline.service.ProjectDisciplineService;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.*;
import xyz.samsami.blokey_land.project.mapper.ProjectMapper;
import xyz.samsami.blokey_land.project.repository.ProjectDslRepository;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.project.service.helper.ProjectAttachHelper;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.service.ProjectSkillService;
import xyz.samsami.blokey_land.skill.service.SkillService;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.mapper.TaskMapper;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {
    private final BlokeyService blokeyService;
    private final MemberService memberService;
    private final SkillService skillService;
    private final ProjectSkillService projectSkillService;
    private final DisciplineService disciplineService;
    private final ProjectDisciplineService projectDisciplineService;
    private final ProjectRepository repository;
    private final ProjectDslRepository dslRepository;
    private final ProjectAttachHelper projectAttachHelper;

    @Transactional
    public void createProject(ProjectReqCreateDto dto, String blokeyId) {
        UUID id = UUID.fromString(blokeyId);
        Blokey blokey = blokeyService.findBlokeyByBlokeyId(id);

        if (blokey == null) throw new CommonException(ExceptionType.NOT_FOUND, null);

        Project project = repository.save(ProjectMapper.toEntity(dto));
        memberService.createMember(project, blokey, RoleType.LEADER);

        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            for (Long skillId : dto.getSkills()) addSkillToProject(project, skillId);
        }

        if (dto.getDisciplines() != null && !dto.getDisciplines().isEmpty()) {
            for (Long disciplineId : dto.getDisciplines()) addDisciplineToProject(project, disciplineId);
        }
    }

    public List<ProjectOnlyRespDto> readAllProjects(String blokeyId) {
        List<ProjectOnlyRespDto> list = repository.findProjectsByBlokeyId(UUID.fromString(blokeyId));
        return projectAttachHelper.attachAll(list);
    }

    public List<ProjectWithTaskRespDto> readAllProjectsWithTasks(String blokeyId) {
        List<Project> projects = repository.findProjectsWithTasksByBlokeyId(UUID.fromString(blokeyId));

        List<ProjectWithTaskRespDto> list = projects.stream()
            .map(project -> {
                List<TaskRespDto> taskRespDtoList = project.getTasks().stream()
                    .map(TaskMapper::toRespDto)
                    .toList();

                return ProjectMapper.toRespDtoWithTaskDtoList(project, taskRespDtoList);
            })
            .toList();

        return projectAttachHelper.attachAll(list);
    }

    public Slice<ProjectOnlyRespDto> readProjectsSlice(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        Slice<ProjectOnlyRespDto> slice = dslRepository.readProjectsSlice(dto, blokeyId, pageable);
        List<ProjectOnlyRespDto> withSkillsAndDisciplines = projectAttachHelper.attachAll(slice.getContent());
        return new SliceImpl<>(withSkillsAndDisciplines, pageable, slice.hasNext());
    }

    public Page<ProjectOnlyRespDto> readProjectsPage(ProjectReqReadDto dto, String blokeyId, Pageable pageable) {
        Page<ProjectOnlyRespDto> page = dslRepository.readProjectsPage(dto, blokeyId, pageable);
        List<ProjectOnlyRespDto> withSkillsAndDisciplines = projectAttachHelper.attachAll(page.getContent());
        return new PageImpl<>(withSkillsAndDisciplines, pageable, page.getTotalElements());
    }

    public ProjectOnlyRespDto readProjectByProjectId(Long projectId) {
        ProjectOnlyRespDto dto = ProjectMapper.toRespDto(findProjectByProjectId(projectId));
        List<ProjectOnlyRespDto> withSkillsAndDisciplines = projectAttachHelper.attachAll(List.of(dto));
        return withSkillsAndDisciplines.getFirst();
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
            syncCollectionById(
                project.getSkills().stream().map(ps -> ps.getSkill().getId()).collect(Collectors.toSet()),
                new HashSet<>(dto.getSkills()),
                skillId -> addSkillToProject(project, skillId),
                skillId -> removeSkillFromProject(project, skillId)
            );
        }

        if (dto.getDisciplines() != null) {
            syncCollectionById(
                project.getDisciplines().stream().map(pd -> pd.getDiscipline().getId()).collect(Collectors.toSet()),
                new HashSet<>(dto.getDisciplines()),
                disciplineId -> addDisciplineToProject(project, disciplineId),
                disciplineId -> removeDisciplineFromProject(project, disciplineId)
            );
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

    @Transactional
    public void addDisciplineToProject(Project project, Long disciplineId) {
        Discipline discipline = disciplineService.findDisciplineByDisciplineId(disciplineId);

        projectDisciplineService.create(project, discipline);
    }

    @Transactional
    public void removeDisciplineFromProject(Project project, Long disciplineId) {
        Discipline discipline = disciplineService.findDisciplineByDisciplineId(disciplineId);

        projectDisciplineService.delete(project, discipline);
    }

    private <T, ID> void syncCollectionById(
        Collection<ID> currentIds,
        Collection<ID> newIds,
        Consumer<ID> adder,
        Consumer<ID> remover
    ) {
        Set<ID> current = new HashSet<>(currentIds);
        Set<ID> target = new HashSet<>(newIds);

        for (ID id : target) {
            if (!current.contains(id)) adder.accept(id);
        }

        for (ID id : current) {
            if (!target.contains(id)) remover.accept(id);
        }
    }
}