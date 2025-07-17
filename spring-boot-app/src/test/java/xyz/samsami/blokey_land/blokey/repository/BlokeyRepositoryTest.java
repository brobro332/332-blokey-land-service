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

    UUID firstBlokeyId;
    Blokey firstBlokey;

    UUID secondBlokeyId;
    Blokey secondBlokey;

    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;

    @BeforeEach
    void setUp() {
        firstBlokeyId = UUID.randomUUID();
        secondBlokeyId = UUID.randomUUID();

        firstBlokey = repository.save(
            Blokey.builder()
                .id(firstBlokeyId)
                .nickname(nickname + "_1")
                .bio(bio + "_1")
                .build()
        );
        secondBlokey = repository.save(
            Blokey.builder()
                .id(secondBlokeyId)
                .nickname(nickname + "_2")
                .bio(bio + "_2")
                .build()
        );

        // given
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
                .blokey(firstBlokey)
                .build()
        );

        offerRepository.save(
            Offer.builder()
                .project(project)
                .blokey(secondBlokey)
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

        assertThat(dto.getId()).isEqualTo(secondBlokey.getId());
        assertThat(dto.getNickname()).isEqualTo(secondBlokey.getNickname());
        assertThat(dto.getBio()).isEqualTo(secondBlokey.getBio());
        assertThat(dto.isHasPendingOffer()).isTrue();
    }
}