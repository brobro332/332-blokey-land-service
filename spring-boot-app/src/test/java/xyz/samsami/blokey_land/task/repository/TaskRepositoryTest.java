package xyz.samsami.blokey_land.task.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.task.domain.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskRepositoryTest extends ContainerBaseTest {
    @Autowired EntityManager entityManager;
    @Autowired TaskRepository repository;
    @Autowired ProjectRepository projectRepository;
    @Autowired BlokeyRepository blokeyRepository;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;
    String title = "테스트_제목_텍스트";
    String description = "테스트_설명_텍스트";
    String imageUrl = "테스트_이미지_URL_텍스트";

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = blokeyRepository.save(Blokey.builder().id(blokeyId).nickname(nickname).bio(bio).build());

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


        for (int i = 1; i <= 5; i++) {
            repository.save(
                Task.builder()
                    .title(title + "_" + i)
                    .description(description + "_" + i)
                    .project(project)
                    .assignee(blokeyId)
                    .estimatedStartDate(LocalDate.now())
                    .estimatedEndDate(LocalDate.now())
                    .actualStartDate(LocalDate.now())
                    .actualEndDate(LocalDate.now())
                    .build()
            );
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("유효한 파라미터가 주어진다면_태스크를 조회할 때_객체 목록을 반환해야 한다.")
    void givenValidParameter_whenFindAllByProjectId_thenReturnsDto() {
        // when
        List<Task> tasks = repository.findAllByProjectId(project.getId());

        // then
        assertThat(tasks).hasSize(5);
        assertThat(tasks).extracting("title")
            .containsExactlyInAnyOrder(
                "테스트_제목_텍스트_1",
                "테스트_제목_텍스트_2",
                "테스트_제목_텍스트_3",
                "테스트_제목_텍스트_4",
                "테스트_제목_텍스트_5"
            );
    }
}