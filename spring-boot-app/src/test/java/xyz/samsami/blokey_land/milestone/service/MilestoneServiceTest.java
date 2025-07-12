package xyz.samsami.blokey_land.milestone.service;

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
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.mapper.MilestoneMapper;
import xyz.samsami.blokey_land.milestone.repository.MilestoneRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.service.TaskService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceTest {
    @InjectMocks private MilestoneService service;
    @Mock private ProjectService projectService;
    @Mock private TaskService taskService;
    @Mock private MilestoneRepository repository;

    private Milestone milestone;
    private Long projectId;
    private Project project;

    @BeforeEach
    void setUp() {
        milestone = mock(Milestone.class);

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

    @DisplayName("Milestone을 저장할 때 모든 필수 값이 저장되어야 한다.")
    @Test
    void givenValidParameter_whenCreateMilestone_thenAllFieldsShouldBeSaved() {
        // given
        try (MockedStatic<MilestoneMapper> mocked = mockStatic(MilestoneMapper.class)) {
            MilestoneReqCreateDto dto = MilestoneReqCreateDto.builder()
                .title("제목")
                .description("설명")
                .dueDate(LocalDate.now())
                .build();

            mocked.when(() -> MilestoneMapper.toEntity(dto, project))
                .thenReturn(milestone);

            when(projectService.findProjectByProjectId(projectId)).thenReturn(project);

            // when
            service.createMilestone(projectId, dto);

            // then
            verify(repository).save(milestone);
        }
    }

    @DisplayName("유효한 파라미터로 Milestone 수정 시 필드 값이 수정되어야 한다.")
    @Test
    void givenValidParameter_whenUpdateMilestoneByMilestoneId_thenMilestoneFieldsUpdated() {
        // given
        Long milestoneId = 1L;
        MilestoneReqUpdateDto dto = MilestoneReqUpdateDto.builder()
            .title("새 제목")
            .description("새 설명")
            .dueDate(LocalDate.now())
            .build();

        when(repository.findById(milestoneId)).thenReturn(Optional.of(milestone));

        // when
        service.updateMilestoneByMilestoneId(milestoneId, dto);

        // then
        verify(milestone).updateTitle(dto.getTitle());
        verify(milestone).updateDescription(dto.getDescription());
        verify(milestone).updateDueDate(dto.getDueDate());
    }

    @DisplayName("유효한 ID로 Milestone 삭제 시 레포지토리와 서비스가 호출되어야 한다.")
    @Test
    void givenValidId_whenDeleteMilestoneByMilestoneId_thenCallRepositoryAndService() {
        // given
        Long milestoneId = 1L;

        when(repository.findById(milestoneId)).thenReturn(Optional.of(milestone));

        // when
        service.deleteMilestoneByMilestoneId(milestoneId);

        // then
        verify(repository).delete(milestone);
        verify(taskService).clearMilestoneFromTasks(milestone);
    }

    @DisplayName("유효한 파라미터가 주어지면 Task 데이터에 Milestone 필드 값이 설정되어야 한다.")
    @Test
    void givenValidParameter_whenSetMilestoneToTask_thenTaskUpdated() {
        // given
        Task task = mock(Task.class);

        Long taskId = 1L;
        Long milestoneId = 2L;

        when(repository.findById(milestoneId)).thenReturn(Optional.of(milestone));
        when(taskService.findTaskByTaskId(taskId)).thenReturn(task);

        // when
        service.setMilestoneToTask(taskId, milestoneId);

        // then
        verify(task).updateMilestone(milestone);
    }

    @DisplayName("존재하는 ID로 Milestone 조회 시 해당 객체가 반환되어야 한다.")
    @Test
    void givenValidMilestoneId_whenFindMilestone_thenReturnMilestone() {
        // given
        Long milestoneId = 1L;

        when(repository.findById(milestoneId)).thenReturn(Optional.of(milestone));

        // when
        Milestone found = service.findMilestoneByMilestoneId(milestoneId);

        // then
        assertEquals(milestone, found);
    }

    @DisplayName("존재하지 않는 ID로 Milestone 조회 시 예외가 발생해야 한다.")
    @Test
    void givenInvalidMilestoneId_whenFindMilestone_thenThrowException() {
        // given
        Long milestoneId = 1L;
        when(repository.findById(milestoneId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findMilestoneByMilestoneId(milestoneId));
    }
}