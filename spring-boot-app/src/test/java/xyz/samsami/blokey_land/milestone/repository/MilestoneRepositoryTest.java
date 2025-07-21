package xyz.samsami.blokey_land.milestone.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired MilestoneRepository repository;
    @Autowired ProjectRepository projectRepository;

    Project project;
    String title = "테스트_제목_텍스트";
    String description = "테스트_설명_텍스트";
    String imageUrl = "테스트_이미지_URL_텍스트";

    Milestone milestone;

    @BeforeEach
    void setUp() {
        project = projectRepository.save(
            Project.builder()
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .isPrivate(true)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build()
        );

        milestone = repository.save(
            Milestone.builder()
                .title(title)
                .description(description)
                .dueDate(LocalDate.of(2025, 7, 12))
                .project(project)
                .build()
        );
    }


    @Test
    @DisplayName("유효한 ID가 주어진다면_프로젝트를 조회할 때_응답 객체가 반환되어야 한다.")
    void givenValidId_whenFindDtoByProject_thenReturnsDto() {
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