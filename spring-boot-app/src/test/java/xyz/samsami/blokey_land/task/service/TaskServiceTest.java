package xyz.samsami.blokey_land.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskReqUpdateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.mapper.TaskMapper;
import xyz.samsami.blokey_land.task.repository.TaskRepository;
import xyz.samsami.blokey_land.task.type.PriorityType;
import xyz.samsami.blokey_land.task.type.TaskStatusType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks private TaskService service;
    @Mock private ProjectService projectService;
    @Mock private TaskRepository repository;

    private Task task;
    private Long taskId;
    private Long projectId;
    private Project project;

    @BeforeEach
    void setUp() {
        task = mock(Task.class);
        taskId = 1L;

        projectId= 1L;
        project = Project.builder()
            .id(projectId)
            .title("제목")
            .description("설명")
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();
    }

    @DisplayName("태스크를 저장할 때 모든 필수 값이 저장되어야 한다.")
    @Test
    void givenValidParameter_whenCreateTask_thenAllFieldsShouldBeSaved() {
        // given
        UUID assigneeId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        TaskReqCreateDto dto = TaskReqCreateDto.builder()
            .title("제목")
            .description("설명")
            .projectId(1L)
            .assignee(assigneeId)
            .priority(PriorityType.HIGH)
            .progress(80)
            .estimatedStartDate(now)
            .estimatedEndDate(now.plusDays(7))
            .actualStartDate(now)
            .actualEndDate(now.plusDays(5))
            .build();

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);

        Task task = Task.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .assignee(assigneeId)
            .priority(dto.getPriority())
            .progress(dto.getProgress())
            .estimatedStartDate(dto.getEstimatedStartDate())
            .estimatedEndDate(dto.getEstimatedEndDate())
            .actualStartDate(dto.getActualStartDate())
            .actualEndDate(dto.getActualEndDate())
            .project(project)
            .build();

        TaskRespDto expectedRespDto = TaskRespDto.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .assignee(task.getAssignee())
            .priority(task.getPriority())
            .progress(task.getProgress())
            .estimatedStartDate(task.getEstimatedStartDate())
            .estimatedEndDate(task.getEstimatedEndDate())
            .actualStartDate(task.getActualStartDate())
            .actualEndDate(task.getActualEndDate())
            .projectId(projectId)
            .build();

        try (MockedStatic<TaskMapper> mocked = mockStatic(TaskMapper.class)) {
            mocked.when(() -> TaskMapper.toEntity(dto, project)).thenReturn(task);
            mocked.when(() -> TaskMapper.toRespDto(task)).thenReturn(expectedRespDto);

            when(repository.save(task)).thenReturn(task);

            // when
            TaskRespDto result = service.createTask(dto);

            // then
            verify(repository).save(task);

            assertEquals(expectedRespDto.getId(), result.getId());
            assertEquals(expectedRespDto.getTitle(), result.getTitle());
            assertEquals(expectedRespDto.getDescription(), result.getDescription());
            assertEquals(expectedRespDto.getAssignee(), result.getAssignee());
            assertEquals(expectedRespDto.getPriority(), result.getPriority());
            assertEquals(expectedRespDto.getProgress(), result.getProgress());
            assertEquals(expectedRespDto.getEstimatedStartDate(), result.getEstimatedStartDate());
            assertEquals(expectedRespDto.getEstimatedEndDate(), result.getEstimatedEndDate());
            assertEquals(expectedRespDto.getActualStartDate(), result.getActualStartDate());
            assertEquals(expectedRespDto.getActualEndDate(), result.getActualEndDate());
            assertEquals(expectedRespDto.getProjectId(), result.getProjectId());
        }
    }

    @Test
    @DisplayName("유효한 파라미터가 주어졌을 때 정보가 수정되어야 한다.")
    void givenValidParameter_whenUpdateTaskByTaskId_thenTaskShouldBeUpdated() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        TaskReqUpdateDto dto = TaskReqUpdateDto.builder()
            .title("수정 제목")
            .description("수정 설명")
            .assignee(UUID.randomUUID())
            .progress(50)
            .status(TaskStatusType.IN_PROGRESS)
            .priority(PriorityType.MEDIUM)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(3))
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now().plusDays(1))
            .build();

        // when
        service.updateTaskByTaskId(taskId, dto);

        // then
        verify(repository).findById(taskId);
        verify(task).updateTitle(dto.getTitle());
        verify(task).updateDescription(dto.getDescription());
        verify(task).updateAssignee(dto.getAssignee());
        verify(task).updateProgress(dto.getProgress());
        verify(task).updateStatus(dto.getStatus());
        verify(task).updatePriority(dto.getPriority());
        verify(task).updateEstimatedStartDate(dto.getEstimatedStartDate());
        verify(task).updateEstimatedEndDate(dto.getEstimatedEndDate());
        verify(task).updateActualStartDate(dto.getActualStartDate());
        verify(task).updateActualEndDate(dto.getActualEndDate());
    }

    @Test
    @DisplayName("유효한 ID가 주어졌을 때 태스크를 삭제해야 한다.")
    void givenValidId_whenDeleteTask_thenRepositoryDeleteCalled() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        service.deleteTaskByTaskId(taskId);

        // then
        verify(repository).findById(taskId);
        verify(repository).delete(task);
    }

    @Test
    @DisplayName("마일스톤이 주어졌을 때 관련 태스크의 마일스톤이 제거되어야 한다.")
    void givenMilestone_whenClearMilestoneFromTasks_thenRepositoryMethodCalled() {
        // given
        Milestone milestone = mock(Milestone.class);

        // when
        service.clearMilestoneFromTasks(milestone);

        // then
        verify(repository).clearMilestoneFromTasks(milestone);
    }

    @Test
    @DisplayName("유효한 ID가 주어졌을 때 태스크 조회 시 객체를 반환해야 한다.")
    void givenValidTaskId_whenFindTask_thenReturnTask() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        Task found = service.findTaskByTaskId(taskId);

        // then
        verify(repository).findById(taskId);
        assertEquals(task, found);
    }

    @Test
    @DisplayName("유효하지 않은 태스크 ID로 조회 시 예외를 던진다")
    void givenInvalidTaskId_whenFindTask_thenThrowException() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findTaskByTaskId(taskId));
        verify(repository).findById(taskId);
    }
}