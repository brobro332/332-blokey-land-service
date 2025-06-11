package xyz.samsami.blokey_land.milestone.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqCreateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqUpdateDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.milestone.repository.MilestoneRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.task.service.TaskService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceTest {
    @InjectMocks
    MilestoneService service;

    @Mock
    TaskService taskService;

    @Mock
    ProjectService projectService;

    @Mock
    MilestoneRepository repository;

    @Test
    void createMilestone_validInput_true() {
        // given
        Long projectId = 1L;
        MilestoneReqCreateDto dto = MilestoneReqCreateDto.builder()
            .title("마일스톤 A")
            .description("테스트 마일스톤입니다.")
            .dueDate(LocalDate.now())
            .build();
        Project project = mock(Project.class);

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);

        // when
        service.createMilestone(projectId, dto);

        // then
        ArgumentCaptor<Milestone> captor = ArgumentCaptor.forClass(Milestone.class);
        verify(repository).save(captor.capture());

        Milestone capture = captor.getValue();

        assertEquals(capture.getTitle(), dto.getTitle());
        assertEquals(capture.getDescription(), dto.getDescription());
        assertEquals(capture.getDueDate(), dto.getDueDate());
    }

    @Test
    void readMilestones_validInput_true() {
        // given
        Long projectId = 1L;
        Long milestoneId = 1L;
        Project project = mock(Project.class);
        Pageable pageable = PageRequest.of(0, 10);
        List<MilestoneRespDto> list = List.of(
            new MilestoneRespDto(milestoneId, "마일스톤 A", "테스트 마일스톤입니다.", LocalDate.now(), projectId)
        );
        Page<MilestoneRespDto> page = new PageImpl<>(list, pageable, list.size());

        when(repository.findByProject(projectId, pageable)).thenReturn(page);

        // when
        Page<MilestoneRespDto> result = service.readMilestonesByProjectId(projectId, pageable);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().getFirst().getMilestoneId());
        assertEquals("마일스톤 A", result.getContent().getFirst().getTitle());
        assertEquals("테스트 마일스톤입니다.", result.getContent().getFirst().getDescription());
    }

    @Test
    void updateMilestone_validInput_true() {
        // given
        Long milestoneId = 1L;
        Milestone milestone = mock(Milestone.class);

        MilestoneReqUpdateDto dto = MilestoneReqUpdateDto.builder()
            .title("마일스톤 A")
            .description("테스트 마일스톤입니다.")
            .dueDate(LocalDate.now())
            .build();

        MilestoneService spyService = spy(service);
        doReturn(milestone).when(spyService).findMilestoneByMilestoneId(milestoneId);

        // when
        spyService.updateMilestoneByMilestoneId(milestoneId, dto);

        // then
        verify(milestone).updateTitle("마일스톤 A");
    }

    @Test
    void deleteMilestoneByMilestoneId_validInput_true() {
        // given
        Long projectId = 1L;
        Long milestoneId = 1L;
        Project project = mock(Project.class);
        Milestone milestone = new Milestone(milestoneId, "마일스톤 A", "테스트 마일스톤입니다.", LocalDate.now(), project);

        MilestoneService spyService = spy(service);
        doReturn(milestone).when(spyService).findMilestoneByMilestoneId(milestoneId);

        // when
        spyService.deleteMilestoneByMilestoneId(milestoneId);

        // then
        verify(repository).delete(milestone);
        verify(taskService).clearMilestoneFromTasks(milestone);
    }
}