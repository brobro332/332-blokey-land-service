package xyz.samsami.blokey_land.task.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.dto.TaskReqCreateDto;
import xyz.samsami.blokey_land.task.dto.TaskReqUpdateDto;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.repository.TaskRepository;
import xyz.samsami.blokey_land.task.type.PriorityType;
import xyz.samsami.blokey_land.task.type.StatusType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @InjectMocks
    TaskService service;

    @Mock
    ProjectService projectService;

    @Mock
    TaskRepository repository;

    @Test
    void createTask_validInput_true() {
        // given
        Long projectId = 1L;

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);

        TaskReqCreateDto dto = TaskReqCreateDto.builder()
            .title("태스크 도메인 개발")
            .description("CRUD API 개발")
            .assignee(UUID.randomUUID())
            .progress(90)
            .status(StatusType.IN_PROGRESS)
            .priority(PriorityType.LOW)
            .projectId(projectId)
            .build();

        // when
        service.createTask(dto);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(repository, times(1)).save(captor.capture());
        Task capturedTask = captor.getValue();

        assertEquals(dto.getTitle(), capturedTask.getTitle());
        assertEquals(dto.getDescription(), capturedTask.getDescription());
        assertEquals(dto.getAssignee(), capturedTask.getAssignee());
        assertEquals(dto.getProgress(), capturedTask.getProgress());
        assertEquals(dto.getStatus(), capturedTask.getStatus());
        assertEquals(dto.getPriority(), capturedTask.getPriority());
        assertEquals(dto.getEstimatedStartDate(), capturedTask.getEstimatedStartDate());
        assertEquals(dto.getEstimatedEndDate(), capturedTask.getEstimatedEndDate());
        assertEquals(dto.getActualStartDate(), capturedTask.getActualStartDate());
        assertEquals(dto.getActualEndDate(), capturedTask.getActualEndDate());
    }

    @Test
    void readTaskByTaskId_validInput_true() {
        // given
        Long projectId = 1L;

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        Long taskId = 1L;

        Task task = Task.builder()
            .id(taskId)
            .title("태스크 도메인 개발")
            .description("CRUD API 개발")
            .assignee(UUID.randomUUID())
            .progress(90)
            .status(StatusType.IN_PROGRESS)
            .priority(PriorityType.LOW)
            .project(project)
            .build();

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        TaskRespDto result = service.readTaskByTaskId(taskId);

        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(task.getAssignee(), result.getAssignee());
        assertEquals(task.getProgress(), result.getProgress());
        assertEquals(task.getStatus(), result.getStatus());
        assertEquals(task.getPriority(), result.getPriority());
        assertEquals(task.getEstimatedStartDate(), result.getEstimatedStartDate());
        assertEquals(task.getEstimatedEndDate(), result.getEstimatedEndDate());
        assertEquals(task.getActualStartDate(), result.getActualStartDate());
        assertEquals(task.getActualEndDate(), result.getActualEndDate());

        verify(repository, times(1)).findById(taskId);
    }

    @Test
    void updateTaskByTaskId_validInput_true() {
        // given
        Long projectId = 1L;

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        Long taskId = 1L;

        Task task = Task.builder()
            .id(taskId)
            .title("태스크 도메인 개발")
            .description("CRUD API 개발")
            .assignee(UUID.randomUUID())
            .progress(90)
            .status(StatusType.IN_PROGRESS)
            .priority(PriorityType.LOW)
            .project(project)
            .build();

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        TaskReqUpdateDto dto = TaskReqUpdateDto.builder()
            .title("마일스톤 도메인 개발")
            .description("패키지 구조 생성")
            .assignee(UUID.randomUUID())
            .progress(50)
            .status(StatusType.TODO)
            .priority(PriorityType.LOW)
            .build();

        // when
        service.updateTaskByTaskId(taskId, dto);
        TaskRespDto result = service.readTaskByTaskId(taskId);

        // then
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getAssignee(), result.getAssignee());
        assertEquals(dto.getProgress(), result.getProgress());
        assertEquals(dto.getStatus(), result.getStatus());
        assertEquals(dto.getPriority(), result.getPriority());
        assertEquals(dto.getEstimatedStartDate(), result.getEstimatedStartDate());
        assertEquals(dto.getEstimatedEndDate(), result.getEstimatedEndDate());
        assertEquals(dto.getActualStartDate(), result.getActualStartDate());
        assertEquals(dto.getActualEndDate(), result.getActualEndDate());
    }

    @Test
    void deleteTaskByTaskId_validInput_true() {
        // given
        Long projectId = 1L;

        Project project = Project.builder()
            .id(projectId)
            .title("BLOKEY-LAND PROJECT")
            .description("GitHub 기반 프로젝트 관리 및 팀원 매칭 서비스입니다.")
            .ownerId(UUID.randomUUID())
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now().plusDays(2))
            .actualStartDate(LocalDate.now().plusDays(3))
            .actualEndDate(LocalDate.now().plusDays(4))
            .build();

        Long taskId = 1L;

        Task task = Task.builder()
            .id(taskId)
            .title("태스크 도메인 개발")
            .description("CRUD API 개발")
            .assignee(UUID.randomUUID())
            .progress(90)
            .status(StatusType.IN_PROGRESS)
            .priority(PriorityType.LOW)
            .project(project)
            .build();

        when(repository.findById(taskId)).thenReturn(Optional.of(task));

        // when
        service.deleteTaskByTaskId(taskId);

        // then
        verify(repository, times(1)).delete(task);
    }

    @Test
    void deleteTaskByTaskId_invalidInput_false() {
        // given
        Long taskId = 999L;

        when(repository.findById(taskId)).thenReturn(Optional.empty());

        // when & then
        CommonException e = assertThrows(CommonException.class, () ->
            service.deleteTaskByTaskId(taskId)
        );

        assertEquals(ExceptionType.NOT_FOUND, e.getException());
    }
}