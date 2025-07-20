package xyz.samsami.blokey_land.project.repository;

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
import xyz.samsami.blokey_land.discipline.domain.Discipline;
import xyz.samsami.blokey_land.discipline.domain.ProjectDiscipline;
import xyz.samsami.blokey_land.discipline.repository.DisciplineRepository;
import xyz.samsami.blokey_land.discipline.repository.ProjectDisciplineRepository;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.dto.ProjectOnlyRespDto;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.repository.ProjectSkillRepository;
import xyz.samsami.blokey_land.skill.repository.SkillRepository;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectRepositoryTest extends ContainerBaseTest {
    @Autowired EntityManager entityManager;
    @Autowired ProjectRepository repository;
    @Autowired BlokeyRepository blokeyRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired SkillRepository skillRepository;
    @Autowired ProjectSkillRepository projectSkillRepository;
    @Autowired DisciplineRepository disciplineRepository;
    @Autowired ProjectDisciplineRepository projectDisciplineRepository;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project1;
    Project project2;
    String title = "테스트_제목_텍스트";
    String description = "테스트_설명_텍스트";
    String imageUrl = "테스트_이미지_URL_텍스트";

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = blokeyRepository.save(
            Blokey.builder()
                .id(blokeyId)
                .nickname(nickname)
                .bio(bio)
                .build()
        );

        project1 = repository.save(Project.builder()
            .title(title + "_1")
            .description(description + "_1")
            .imageUrl(imageUrl + "_1")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());

        project2 = repository.save(Project.builder()
            .title(title + "_2")
            .description(description + "_2")
            .imageUrl(imageUrl + "_2")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());

        Skill skill = Skill.builder().name("java").displayName("Java").build();
        skillRepository.save(skill);
        ProjectSkill projectSkill = ProjectSkill.builder().project(project1).skill(skill).build();
        projectSkillRepository.save(projectSkill);
        project1.addSkill(projectSkill);

        Discipline discipline = disciplineRepository.findById(1L).orElseThrow();
        ProjectDiscipline projectDiscipline = ProjectDiscipline.builder().project(project1).discipline(discipline).build();
        projectDisciplineRepository.save(projectDiscipline);
        project1.addDiscipline(projectDiscipline);

        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project1).blokey(blokey).build());
        memberRepository.save(Member.builder().role(RoleType.LEADER).project(project2).blokey(blokey).build());

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("사용자 ID가 주어진다면_프로젝트를 조회할 때_해당 사용자의 프로젝트 목록을 반환하되 필드에 리더 여부를 포함하고 스킬과 분야를 제외한다.")
    void givenBlokeyId_whenFindProjectsByBlokeyId_thenReturnsProjects() {
        // when
        List<ProjectOnlyRespDto> result = repository.findProjectsByBlokeyId(blokeyId);

        // then
        assertThat(result.size()).isEqualTo(2);
        for (ProjectOnlyRespDto project : result) {
            assertThat(project.getTitle()).contains(title);
            assertThat(project.getDescription()).contains(description);
            assertThat(project.getImageUrl()).contains(imageUrl);
            assertThat(project.getIsLeader()).isTrue();

            /* NOTE: 서비스에서 처리 */
            if (project.getTitle().contains("_1")) {
                assertThat(project.getSkills().size()).isEqualTo(0);
                assertThat(project.getDisciplines().size()).isEqualTo(0);
            }
        }
    }

    @Test
    @DisplayName("사용자 ID가 주어진다면_프로젝트 및 태스크 목록을 조회할 때_프로젝트 목록과 해당 프로젝트의 태스크 목록을 함께 반환한다.")
    void givenBlokeyId_whenFindProjectsWithTasksByBlokeyId_thenReturnsProjectsWithTasks() {
        // given
        taskRepository.save(Task.builder()
            .title("테스트_제목_텍스트")
            .description("테스트_설명_텍스트")
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
        assertThat(result.size()).isEqualTo(2);
        for (Project project : result) {
            assertThat(project.getTitle()).contains(title);
            assertThat(project.getDescription()).contains(description);
            assertThat(project.getImageUrl()).contains(imageUrl);

            /* NOTE: 엔티티를 그대로 반환하므로 스킬과 분야에 대한 그래프 탐색 가능 */
            if (project.getTitle().contains("_1")) {
                assertThat(project.getSkills().size()).isEqualTo(1);
                assertThat(project.getDisciplines().size()).isEqualTo(1);
                assertThat(result.getFirst().getTasks().size()).isEqualTo(1);
            }
        }
    }
}