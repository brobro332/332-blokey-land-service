package xyz.samsami.blokey_land.project.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectRepositoryTest extends ContainerBaseTest {
    @Autowired private ProjectRepository repository;
    @Autowired private BlokeyRepository blokeyRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TaskRepository taskRepository;

    UUID blokeyId;
    Blokey blokey;
    Project project1;
    Project project2;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        project1 = repository.save(Project.builder()
            .title("제목 1")
            .description("설명 1")
            .imageUrl("이미지 URL 1")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());

        project2 = repository.save(Project.builder()
            .title("제목 2")
            .description("설명 2")
            .imageUrl("이미지 URL 2")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());

        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project1).blokey(blokey).build());
        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project2).blokey(blokey).build());
    }

    @Test
    @DisplayName("사용자 ID로 프로젝트 조회 시 해당 사용자의 프로젝트 목록과 해당 프로젝트 내 역할이 함께 반환된다")
    void givenProjectId_whenFindDtoByProjectId_thenReturnsPagedMembers() {
        // when
        List<ProjectOnlyRespDto> result = repository.findProjectsWithRoleByBlokeyId(blokeyId);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result).extracting("title")
            .containsExactlyInAnyOrder("제목 1", "제목 2");
    }

    @Test
    @DisplayName("사용자 ID로 프로젝트 및 태스크 목록 조회 시 프로젝트 목록과 해당 프로젝트의 태스크 목록이 함께 반환된다.")
    void givenBlokeyId_whenFindProjectsWithTasksByBlokeyId_thenReturnsProjectsWithTasks() {
        // given
        taskRepository.save(Task.builder()
            .title("제목 1")
            .description("설명 1")
            .project(project1)
            .milestone(null)
            .assignee(blokeyId)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build()
        );

        // when
        List<Project> result = repository.findProjectsWithTasksByBlokeyId(blokeyId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getTitle()).isEqualTo("제목 1");
        assertThat(result.getFirst().getTasks()).isNotNull();
    }
}