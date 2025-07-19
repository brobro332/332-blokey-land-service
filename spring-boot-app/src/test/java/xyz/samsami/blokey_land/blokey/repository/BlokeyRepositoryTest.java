package xyz.samsami.blokey_land.blokey.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.dto.BlokeyRespDto;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.member.domain.Member;
import xyz.samsami.blokey_land.member.repository.MemberRepository;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.repository.OfferRepository;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BlokeyRepositoryTest extends ContainerBaseTest {
    @Autowired private BlokeyRepository repository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private OfferRepository offerRepository;
    @Autowired private ProjectRepository projectRepository;

    UUID blokeyId1;
    UUID blokeyId2;
    Blokey blokey1;
    Blokey blokey2;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;

    @BeforeEach
    void setUp() {
        blokeyId1 = UUID.randomUUID();
        blokeyId2 = UUID.randomUUID();

        blokey1 = repository.save(
            Blokey.builder()
                .id(blokeyId1)
                .nickname(nickname + "_1")
                .bio(bio + "_1")
                .build()
        );
        blokey2 = repository.save(
            Blokey.builder()
                .id(blokeyId2)
                .nickname(nickname + "_2")
                .bio(bio + "_2")
                .build()
        );

        Skill skill = Skill.builder().id(1L).name("java").displayName("Java").build();
        BlokeySkill blokeySkill = BlokeySkill.builder().blokey(blokey1).skill(skill).build();
        blokey1.addSkill(blokeySkill);

        project = projectRepository.save(
            Project.builder()
                .title("테스트_프로젝트_제목_텍스트")
                .description("테스트_프로젝트_설명_텍스트")
                .imageUrl("테스트_프로젝트_이미지_URL_텍스트")
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
                .blokey(blokey1)
                .build()
        );

        offerRepository.save(
            Offer.builder()
                .project(project)
                .blokey(blokey2)
                .offerer(OfferType.PROJECT)
                .status(OfferStatusType.PENDING)
                .build()
        );
    }

    @Test
    @DisplayName("프로젝트에 속하지 않은 사용자 정보 및 제안 대기 여부 조회")
    void givenProjectWithMembersAndOffers_whenFindByNotInProject_thenReturnsBlokeyWithPendingOffer() {
        // when
        Page<BlokeyRespDto> result = repository.findByNotInProject(project.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result).hasSize(1);
        BlokeyRespDto dto = result.getContent().getFirst();

        assertThat(dto.getId()).isEqualTo(blokey2.getId());
        assertThat(dto.getNickname()).isEqualTo(blokey2.getNickname());
        assertThat(dto.getBio()).isEqualTo(blokey2.getBio());
        assertThat(dto.getSkills().size()).isEqualTo(0);
        assertThat(dto.isHasPendingOffer()).isTrue();
    }
}