package xyz.samsami.blokey_land.milestone.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqReadDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MilestoneDslRepositoryTest extends ContainerBaseTest {
    @Autowired private MilestoneRepository repository;
    @Autowired private MilestoneDslRepository dslRepository;
    @Autowired private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트 ID가 주어졌을 때 프로젝트에 속하는 응답 DTO 조회")
    void givenParameter_whenReadMilestones_thenReturnsDto() {
        // given
        Project project = projectRepository.save(
            Project.builder()
                .title("제목")
                .description("설명")
                .imageUrl("이미지 URL")
                .isPrivate(true)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build()
        );

        Milestone milestone1 = repository.save(
            Milestone.builder()
                .title("제목")
                .description("설명")
                .dueDate(LocalDate.now())
                .project(project)
                .build()
        );

        repository.save(
            Milestone.builder()
                .title("제목")
                .description("설명")
                .dueDate(LocalDate.now().plusDays(1))
                .project(project)
                .build()
        );

        MilestoneReqReadDto request = new MilestoneReqReadDto();
        request.setTitle("제목");
        request.setDescription("설명");
        request.setMonth(7);
        request.setDueDateFrom(LocalDate.of(2025, 7, 9));
        request.setDueDateTo(LocalDate.of(2025, 7, 12));
        request.setProjectId(project.getId());

        // when
        List<MilestoneRespDto> result = dslRepository.readMilestones(request);

        // then
        assertThat(result).hasSize(1);
        MilestoneRespDto response = result.getFirst();

        assertThat(response.getId()).isEqualTo(milestone1.getId());
        assertThat(response.getTitle()).isEqualTo(milestone1.getTitle());
        assertThat(response.getDescription()).isEqualTo(milestone1.getDescription());
        assertThat(response.getDueDate()).isEqualTo(milestone1.getDueDate());
        assertThat(response.getProjectId()).isEqualTo(project.getId());
    }
}