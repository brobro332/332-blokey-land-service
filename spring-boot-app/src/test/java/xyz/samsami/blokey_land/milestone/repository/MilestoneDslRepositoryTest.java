package xyz.samsami.blokey_land.milestone.repository;

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
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.milestone.domain.Milestone;
import xyz.samsami.blokey_land.milestone.dto.MilestoneReqReadDto;
import xyz.samsami.blokey_land.milestone.dto.MilestoneRespDto;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MilestoneDslRepositoryTest extends ContainerBaseTest {
    @Autowired EntityManager entityManager;
    @Autowired MilestoneRepository repository;
    @Autowired MilestoneDslRepository dslRepository;
    @Autowired ProjectRepository projectRepository;
    @Autowired BlokeyRepository blokeyRepository;
    @Autowired MemberRepository memberRepository;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;
    String title = "테스트_제목_텍스트";
    String description = "테스트_설명_텍스트";
    String imageUrl = "테스트_이미지_URL_텍스트";

    Milestone milestone;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = blokeyRepository.save(Blokey.builder().id(blokeyId).nickname(nickname + "_1").bio(bio + "_1").build());

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

        memberRepository.save(
            Member.builder()
                .role(RoleType.LEADER)
                .project(project)
                .blokey(blokey)
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

        entityManager.flush();
        entityManager.clear();
    }


    @Test
    @DisplayName("프로젝트 ID가 주어졌을 때 프로젝트에 속하는 응답 DTO 조회")
    void givenParameter_whenReadMilestones_thenReturnsDto() {
        // given
        MilestoneReqReadDto request = new MilestoneReqReadDto();
        request.setTitle(title);
        request.setDescription(description);
        request.setMonth(7);
        request.setDueDateFrom(LocalDate.of(2025, 7, 9));
        request.setDueDateTo(LocalDate.of(2025, 7, 12));
        request.setProjectId(project.getId());

        // when
        List<MilestoneRespDto> result = dslRepository.readMilestones(request, blokeyId);

        // then
        assertThat(result).hasSize(1);
        MilestoneRespDto response = result.getFirst();

        assertThat(response.getId()).isEqualTo(milestone.getId());
        assertThat(response.getTitle()).isEqualTo(milestone.getTitle());
        assertThat(response.getDescription()).isEqualTo(milestone.getDescription());
        assertThat(response.getDueDate()).isEqualTo(milestone.getDueDate());
        assertThat(response.getProjectId()).isEqualTo(project.getId());
    }
}