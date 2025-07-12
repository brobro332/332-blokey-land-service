package xyz.samsami.blokey_land.milestone.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MilestoneRepositoryTest extends ContainerBaseTest {
    @Autowired private MilestoneRepository repository;
    @Autowired private ProjectRepository projectRepository;

    @Test
    @DisplayName("프로젝트 ID가 주어졌을 때 프로젝트에 속하는 응답 DTO 조회")
    void givenProjectId_whenFindDtoByProject_thenReturnsDto() {
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

        Milestone milestone = repository.save(Milestone.builder()
            .project(project)
            .title("제목")
            .description("설명")
            .dueDate(LocalDate.now())
            .build());

        // when
        List<MilestoneRespDto> result = repository.findDtoByProject(project.getId());

        // then
        assertThat(result).hasSize(1);
        MilestoneRespDto dto = result.getFirst();

        assertThat(dto.getId()).isEqualTo(milestone.getId());
        assertThat(dto.getTitle()).isEqualTo(milestone.getTitle());
        assertThat(dto.getDescription()).isEqualTo(milestone.getDescription());
        assertThat(dto.getDueDate()).isEqualTo(milestone.getDueDate());
        assertThat(dto.getProjectId()).isEqualTo(project.getId());
    }
}