package xyz.samsami.blokey_land.task.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskDslRepositoryTest extends ContainerBaseTest {
    @Autowired private TaskDslRepository dslRepository;
    @Autowired private TaskRepository repository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private BlokeyRepository blokeyRepository;

    private Project project;

    @BeforeEach
    void setUp() {
        UUID blokeyId = UUID.randomUUID();
        blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        project = projectRepository.save(
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

        for (int i = 1; i <= 5; i++) {
            repository.save(
                Task.builder()
                    .title("제목 " + i)
                    .description("설명 " + i)
                    .project(project)
                    .assignee(blokeyId)
                    .estimatedStartDate(LocalDate.now())
                    .estimatedEndDate(LocalDate.now())
                    .actualStartDate(LocalDate.now())
                    .actualEndDate(LocalDate.now())
                    .build()
            );
        }
    }

    @Test
    @DisplayName("프로젝트 ID가 주어졌을 때 태스크 조회 시 페이지를 반환해야 한다.")
    void givenProjectId_whenReadTasksByProjectId_thenReturnPage() {
        var pageable = Pageable.ofSize(3);
        var result = dslRepository.readTasksByProjectId(project.getId(), pageable);

        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getContent()).extracting(TaskRespDto::getTitle)
            .contains("제목 5", "제목 4", "제목 3");
    }
}