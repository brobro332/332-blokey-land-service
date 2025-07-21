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
    @InjectMocks TaskService service;
    @Mock ProjectService projectService;
    @Mock TaskRepository repository;

    UUID blokeyId;

    Long projectId;
    Project project;
    String title = "테스트_제목_텍스트";
    String description = "테스트_설명_텍스트";
    String imageUrl = "테스트_이미지_URL_텍스트";

    Task task;
    Long taskId;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();

        task = Task.builder()
            .title(title)
            .description(description)
            .project(project)
            .assignee(blokeyId)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();

        projectId= 1L;
        project = Project.builder()
            .id(projectId)
            .title(title)
            .description(description)
            .imageUrl(imageUrl)
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();
    }

    @DisplayName("유효한 파라미터가 주어진다면_태스크를 저장할 때_모든 필수 값이 저장되어야 한다.")
    @Test
    void givenValidParameter_whenCreateTask_thenAllFieldsShouldBeSaved() {
        // given
        LocalDate now = LocalDate.now();
        TaskReqCreateDto dto = TaskReqCreateDto.builder()
            .title(title)
            .description(description)
            .assignee(blokeyId)
            .projectId(1L)
            .priority(PriorityType.HIGH)
            .progress(80)
            .estimatedStartDate(now)
            .estimatedEndDate(now.plusDays(7))
            .actualStartDate(now)
            .actualEndDate(now.plusDays(5))
            .build();

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);

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
    @DisplayName("유효한 파라미터가 주어진다면_태스크를 수정할 때_태스크가 갱신돼야 한다.")
    void givenValidParameter_whenUpdateTaskByTaskId_thenTaskShouldBeUpdated() {
        // given
        task = spy(task);

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        TaskReqUpdateDto dto = TaskReqUpdateDto.builder()
            .title("테스트_수정_제목_텍스트")
            .description("테스트_수정_설명_텍스트")
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
    @DisplayName("유효한 ID가 주어진다면_태스크를 삭제할 때_올바르게 삭제해야 한다.")
    void givenValidId_whenDeleteTask_thenCallsMethod() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        service.deleteTaskByTaskId(taskId);

        // then
        verify(repository).findById(taskId);
        verify(repository).delete(task);
    }

    @Test
    @DisplayName("유효한 ID가 주어진다면_태스크 조회 시_해당 엔티티를 반환해야 한다.")
    void givenValidTaskId_whenFindTask_thenReturnsTask() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        Task result = service.findTaskByTaskId(taskId);

        // then
        verify(repository).findById(taskId);
        assertEquals(task, result);
    }

    @Test
    @DisplayName("유효하지 않은 ID가 주어진다면_태스크를 조회할 때_예외를 던져야 한다.")
    void givenInvalidId_whenFindTask_thenThrowsException() {
        // given
        when(repository.findById(taskId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findTaskByTaskId(taskId));
        verify(repository).findById(taskId);
    }
}