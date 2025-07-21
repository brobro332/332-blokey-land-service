package xyz.samsami.blokey_land.offer.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.repository.BlokeyRepository;
import xyz.samsami.blokey_land.common.ContainerBaseTest;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.repository.ProjectRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OfferRepositoryTest extends ContainerBaseTest {
    @Autowired EntityManager entityManager;
    @Autowired OfferRepository repository;
    @Autowired ProjectRepository projectRepository;
    @Autowired BlokeyRepository blokeyRepository;

    UUID blokeyId;
    Blokey blokey;
    String nickname = "테스트_닉네임_텍스트";
    String bio = "테스트_소개_텍스트";

    Project project;

    @BeforeEach
    void setUp() {
        blokeyId = UUID.randomUUID();
        blokey = blokeyRepository.save(Blokey.builder().id(blokeyId).nickname(nickname).bio(bio).build());

        project = projectRepository.save(
            Project.builder()
                .title("테스트_제목_텍스트")
                .description("테스트_설명_텍스트")
                .imageUrl("테스트_이미지_URL_텍스트")
                .isPrivate(true)
                .estimatedStartDate(LocalDate.now())
                .estimatedEndDate(LocalDate.now())
                .actualStartDate(LocalDate.now())
                .actualEndDate(LocalDate.now())
                .build()
        );

        repository.save(
            Offer.builder()
                .project(project)
                .blokey(blokey)
                .offerer(OfferType.PROJECT)
                .status(OfferStatusType.PENDING)
                .build()
        );

        repository.save(
            Offer.builder()
                .project(project)
                .blokey(blokey)
                .offerer(OfferType.BLOKEY)
                .status(OfferStatusType.PENDING)
                .build()
        );

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("유효한 프로젝트 ID가 주어진다면_제안을 조회할 때_응답 객체를 반환해야 한다.")
    void givenValidProjectId_whenFindDtoByProjectId_thenReturnsDto() {
        // when
        Page<OfferRespDto> result = repository.findDtoByProjectId(
            project.getId(), OfferType.PROJECT, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getProjectId()).isEqualTo(project.getId());
        assertThat(result.getContent().getFirst().getOfferer()).isEqualTo(OfferType.PROJECT);
    }

    @Test
    @DisplayName("유효한 사용자 ID가 주어진다면_제안을 조회할 때_응답 객체를 반환해야 한다.")
    void givenValidBlokeyId_whenFindDtoByBlokeyId_thenReturnDto() {
        // when
        Page<OfferRespDto> result = repository.findDtoByBlokeyId(
            blokeyId, OfferType.BLOKEY, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getBlokeyId()).isEqualTo(blokeyId);
        assertThat(result.getContent().getFirst().getOfferer()).isEqualTo(OfferType.BLOKEY);
    }
}