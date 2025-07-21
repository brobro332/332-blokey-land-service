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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceTest {
    @InjectMocks MilestoneService service;
    @Mock ProjectService projectService;
    @Mock MilestoneRepository repository;

    Long milestoneId;
    Milestone milestone;

    Long projectId;
    Project project;
    String title = "테스트_프로젝트_제목_텍스트";
    String description = "테스트_프로젝트_설명_텍스트";
    String imageUrl = "테스트_프로젝트_이미지_URL_텍스트";

    @BeforeEach
    void setUp() {
        milestoneId = 1L;
        milestone = mock(Milestone.class);

        projectId = 1L;
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

    @DisplayName("유효한 파라미터가 주어진다면_마일스톤을 저장할 때_모든 필수 값이 저장되어야 한다.")
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

    @DisplayName("유효한 파라미터가 주어진다면_마일스톤을 수정할 때_올바르게 갱신되어야 한다.")
    @Test
    void givenValidParameter_whenUpdateMilestoneByMilestoneId_thenMilestoneFieldsUpdated() {
        // given
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

    @DisplayName("유효한 ID가 주어진다면_마일스톤을 삭제할 때_메서드가 호출되어야 한다.")
    @Test
    void givenValidId_whenDeleteMilestoneByMilestoneId_thenCallsMethod() {
        // given
        when(repository.findById(milestoneId)).thenReturn(Optional.of(milestone));

        // when
        service.deleteMilestoneByMilestoneId(milestoneId);

        // then
        verify(repository).delete(milestone);
    }

    @DisplayName("존재하는 ID가 주어진다면_마일스톤을 조회할 때_해당 엔티티가 반환되어야 한다.")
    @Test
    void givenValidId_whenFindMilestone_thenReturnMilestone() {
        // given
        when(repository.findById(milestoneId)).thenReturn(Optional.of(milestone));

        // when
        Milestone result = service.findMilestoneByMilestoneId(milestoneId);

        // then
        assertEquals(milestone, result);
    }

    @DisplayName("존재하지 않는 ID가 주어진다면_마일스톤 조회을 조회할 때_예외가 발생해야 한다.")
    @Test
    void givenInvalidMilestoneId_whenFindMilestone_thenThrowsException() {
        // given
        when(repository.findById(milestoneId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findMilestoneByMilestoneId(milestoneId));
    }
}