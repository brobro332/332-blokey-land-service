package xyz.samsami.blokey_land.project.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
import xyz.samsami.blokey_land.project.dto.ProjectReqReadDto;
import xyz.samsami.blokey_land.skill.domain.ProjectSkill;
import xyz.samsami.blokey_land.skill.domain.Skill;
import xyz.samsami.blokey_land.skill.repository.ProjectSkillRepository;
import xyz.samsami.blokey_land.skill.repository.SkillRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProjectDslRepositoryTest extends ContainerBaseTest {
    @Autowired EntityManager entityManager;
    @Autowired ProjectDslRepository dslRepository;
    @Autowired ProjectRepository repository;
    @Autowired BlokeyRepository blokeyRepository;
    @Autowired MemberRepository memberRepository;
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
    @DisplayName("유효한 파라미터가 주어진다면_프로젝트 슬라이스를 조회할 때_필터링된 프로젝트 목록을 반환하되 필드에 스킬과 분야는 포함하지 않는다.")
    void givenValidParameter_whenReadProjectsSlice_thenReturnFilteredProjects() {
        // given
        ProjectReqReadDto dto = new ProjectReqReadDto();
        dto.setTitle(title);

        Pageable pageable = PageRequest.of(0, 10);
        Slice<ProjectOnlyRespDto> result = dslRepository.readProjectsSlice(dto, blokey.getId().toString(), pageable);

        for (ProjectOnlyRespDto project : result.getContent()) {
            assertThat(project.getTitle()).contains(title);
            assertThat(project.getDescription()).contains(description);
            assertThat(project.getImageUrl()).contains(imageUrl);

            /* NOTE: 서비스에서 처리 */
            if (project.getTitle().contains("_1")) {
                assertThat(project.getSkills().size()).isEqualTo(0);
                assertThat(project.getDisciplines().size()).isEqualTo(0);
            }
        }
    }

    @Test
    @DisplayName("유효한 파라미터가 주어진다면_프로젝트 페이지를 조회할 때_필터링된 프로젝트 목록을 반환하되 필드에 스킬과 분야는 포함하지 않는다.")
    void givenValidParameter_whenReadProjectsPage_thenReturnFilteredProject() {
        // given
        ProjectReqReadDto dto = new ProjectReqReadDto();
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProjectOnlyRespDto> result = dslRepository.readProjectsPage(dto, blokey.getId().toString(), pageable);

        // then
        for (ProjectOnlyRespDto project : result.getContent()) {
            assertThat(project.getTitle()).contains(title);
            assertThat(project.getDescription()).contains(description);
            assertThat(project.getImageUrl()).contains(imageUrl);

            /* NOTE: 서비스에서 처리 */
            if (project.getTitle().contains("_1")) {
                assertThat(project.getSkills().size()).isEqualTo(0);
                assertThat(project.getDisciplines().size()).isEqualTo(0);
            }
        }
    }
}