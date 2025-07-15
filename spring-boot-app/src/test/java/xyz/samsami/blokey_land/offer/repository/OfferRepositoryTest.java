package xyz.samsami.blokey_land.offer.repository;

import jakarta.transaction.Transactional;
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
    @Autowired private OfferRepository repository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private BlokeyRepository blokeyRepository;

    @Test
    @DisplayName("프로젝트 ID가 주어졌을 때 제안 조회 시 DTO를 응답해야 한다.")
    void givenProjectId_whenFindDtoByProjectId_thenReturnDto() {
        // given
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey = blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        Project project = projectRepository.save(Project.builder()
            .title("제목")
            .description("설명")
            .imageUrl("이미지 URL")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());

        repository.save(
            Offer.builder()
                .project(project)
                .blokey(blokey)
                .offerer(OfferType.PROJECT)
                .status(OfferStatusType.PENDING)
                .build()
        );

        // when
        Page<OfferRespDto> result = repository.findDtoByProjectId(
            project.getId(), OfferType.PROJECT, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getProjectId()).isEqualTo(project.getId());
        assertThat(result.getContent().getFirst().getOfferer()).isEqualTo(OfferType.PROJECT);
    }

    @Test
    @DisplayName("사용자 ID가 주어졌을 때 제안 조회 시 DTO를 응답해야 한다.")
    void givenBlokeyId_whenFindDtoByBlokeyId_thenReturnDto() {
        // given
        UUID blokeyId = UUID.randomUUID();
        Blokey blokey = blokeyRepository.save(new Blokey(blokeyId, "닉네임", "소개"));

        Project project = projectRepository.save(Project.builder()
            .title("제목")
            .description("설명")
            .imageUrl("이미지 URL")
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build());

        repository.save(
            Offer.builder()
                .project(project)
                .blokey(blokey)
                .offerer(OfferType.BLOKEY)
                .status(OfferStatusType.PENDING)
                .build()
        );

        // when
        Page<OfferRespDto> result = repository.findDtoByBlokeyId(
            blokey.getId(), OfferType.BLOKEY, PageRequest.of(0, 10));

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getBlokeyId()).isEqualTo(blokey.getId());
        assertThat(result.getContent().getFirst().getOfferer()).isEqualTo(OfferType.BLOKEY);
    }
}