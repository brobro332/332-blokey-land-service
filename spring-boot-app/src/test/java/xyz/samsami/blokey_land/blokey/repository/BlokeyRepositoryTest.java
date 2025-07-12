package xyz.samsami.blokey_land.blokey.repository;

import jakarta.transaction.Transactional;
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

    @Test
    @DisplayName("프로젝트에 속하지 않은 사용자 정보 및 제안 대기 여부 조회")
    void givenProjectWithMembersAndOffers_whenFindByNotInProject_thenReturnsBlokeyWithPendingOffer() {
        // given
        Project project = projectRepository.save(
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

        Blokey blokey1 = repository.save(new Blokey(UUID.randomUUID(), "닉네임 1", "소개 1"));
        Blokey blokey2 = repository.save(new Blokey(UUID.randomUUID(), "닉네임 2", "소개 2"));

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

        // when
        Page<BlokeyRespDto> result = repository.findByNotInProject(project.getId(), PageRequest.of(0, 10));

        // then
        assertThat(result).hasSize(1);
        BlokeyRespDto dto = result.getContent().getFirst();

        assertThat(dto.getId()).isEqualTo(blokey2.getId());
        assertThat(dto.getNickname()).isEqualTo(blokey2.getNickname());
        assertThat(dto.getBio()).isEqualTo(blokey2.getBio());
        assertThat(dto.isHasPendingOffer()).isTrue();
    }
}