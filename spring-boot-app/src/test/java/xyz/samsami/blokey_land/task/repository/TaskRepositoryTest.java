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
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.repository.MilestoneRepository;
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
    @Autowired private TaskRepository repository;
    @Autowired private MilestoneRepository milestoneRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private BlokeyRepository blokeyRepository;
    @Autowired EntityManager entityManager;

    private Project project;
    private Milestone milestone;

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

        milestone = milestoneRepository.save(
            Milestone.builder()
                .title("제목")
                .description("설명")
                .dueDate(LocalDate.now())
                .project(project)
                .build()
        );

        repository.save(
            Task.builder()
                .title("제목 1")
                .description("설명 1")
                .project(project)
                .milestone(milestone)
                .assignee(blokeyId)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build()
        );

        repository.save(
            Task.builder()
                .title("제목 2")
                .description("설명 2")
                .project(project)
                .milestone(milestone)
                .assignee(blokeyId)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build()
        );
    }

    @Test
    @DisplayName("마일스톤이 주어졌을 때 해당 마일스톤이 설정되어 있는 태스크의 마일스톤을 초기화해야 한다.")
    void givenMilestone_whenClearMilestoneFromTasks_thenClearMilestone() {
        List<Task> tasksBefore = repository.findAllByProjectId(project.getId());
        assertThat(tasksBefore.stream().anyMatch(t -> t.getMilestone() != null)).isTrue();

        repository.clearMilestoneFromTasks(milestone);
        entityManager.flush();
        entityManager.clear();

        List<Task> tasksAfter = repository.findAllByProjectId(project.getId());
        assertThat(tasksAfter.stream().allMatch(t -> t.getMilestone() == null)).isTrue();
    }

    @Test
    @DisplayName("유효한 파라미터가 주어졌을 때 객체 목록을 반환해야 한다.")
    void givenValidParameter_whenFindAllByProjectId_thenReturn() {
        // when
        List<Task> tasks = repository.findAllByProjectId(project.getId());

        // then
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting("title")
            .containsExactlyInAnyOrder("제목 1", "제목 2");
    }
}