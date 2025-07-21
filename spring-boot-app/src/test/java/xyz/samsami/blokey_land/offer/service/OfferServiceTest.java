package xyz.samsami.blokey_land.offer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.mapper.OfferMapper;
import xyz.samsami.blokey_land.offer.repository.OfferRepository;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.project.type.ProjectStatusType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    @InjectMocks OfferService service;
    @Mock BlokeyService blokeyService;
    @Mock ProjectService projectService;
    @Mock MemberService memberService;
    @Mock OfferRepository repository;


    UUID blokeyId;
    Blokey blokey;
    Long projectId;
    Project project;
    Long offerId;
    Offer offer;

    @BeforeEach
    void setUp() {
        offerId = 1L;
        offer = mock(Offer.class);

        blokeyId = UUID.randomUUID();
        blokey = Blokey.builder()
            .id(blokeyId)
            .nickname("닉네임")
            .bio("소개")
            .build();

        projectId= 1L;
        project = Project.builder()
            .id(projectId)
            .title("제목")
            .description("설명")
            .status(ProjectStatusType.ACTIVE)
            .isPrivate(true)
            .estimatedStartDate(LocalDate.now())
            .estimatedEndDate(LocalDate.now())
            .actualStartDate(LocalDate.now())
            .actualEndDate(LocalDate.now())
            .build();
    }

    @DisplayName("유효한 파라미터가 주어진다면_제안을 저장할 때_모든 필수 값이 저장되어야 한다.")
    @Test
    void givenValidParameter_whenCreateOffer_thenAllFieldsShouldBeSaved() {
        // given
        OfferReqCreateDto dto = OfferReqCreateDto.builder()
            .projectId(projectId)
            .blokeyId(blokeyId)
            .status(OfferStatusType.PENDING)
            .offerer(OfferType.PROJECT)
            .build();

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);
        when(blokeyService.findBlokeyByBlokeyId(blokeyId)).thenReturn(blokey);

        try (MockedStatic<OfferMapper> mocked = mockStatic(OfferMapper.class)) {
            mocked.when(() -> OfferMapper.toEntity(dto, project, blokey))
                .thenReturn(offer);

            OfferRespDto respDto = new OfferRespDto();
            mocked.when(() -> OfferMapper.toRespDto(offer)).thenReturn(respDto);

            when(repository.save(offer)).thenReturn(offer);

            // when
            OfferRespDto result = service.createOffer(dto);

            // then
            assertEquals(respDto, result);
            verify(repository).save(offer);
        }
    }

    @DisplayName("유효한 파라미터가 주어진다면_제안을 수정할 때_수락할 경우 제안 상태가 갱신되고 멤버가 생성돼야 한다.")
    @Test
    void givenValidParameter_whenUpdateOffer_thenStatusUpdatedAndMemberCreatedIfAccepted() {
        // given
        OfferReqUpdateDto dto = OfferReqUpdateDto.builder()
            .status(OfferStatusType.ACCEPTED)
            .build();

        when(repository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offer.getProject()).thenReturn(project);
        when(offer.getBlokey()).thenReturn(blokey);

        // when
        service.updateOfferByOfferId(offerId, dto);

        // then
        verify(offer).updateStatus(dto.getStatus());
        verify(memberService).createMember(project, blokey, RoleType.MEMBER);
    }

    @DisplayName("유효한 파라미터가 주어진다면_제안을 수정할 때_수락하지 않을 경우 멤버 생성 없이 제안 상태가 갱신돼야 한다.")
    @Test
    void givenValidParameter_whenUpdateOffer_thenOnlyStatusUpdated() {
        // given
        OfferReqUpdateDto dto = OfferReqUpdateDto.builder()
            .status(OfferStatusType.REJECTED)
            .build();

        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        service.updateOfferByOfferId(offerId, dto);

        // then
        verify(offer).updateStatus(dto.getStatus());
        verifyNoInteractions(memberService);
    }

    @DisplayName("유효한 ID가 주어진다면_제안을 삭제할 때_올바르게 삭제돼야 한다.")
    @Test
    void givenValidId_whenDeleteOffer_thenOfferDeleted() {
        // given
        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        service.deleteOffer(offerId);

        // then
        verify(repository).delete(offer);
    }

    @DisplayName("존재하는 ID가 주어진다면_제안을 조회할 때_엔티티를 반환해야 한다.")
    @Test
    void givenExistingId_whenFindOffer_thenReturnOffer() {
        // given
        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        Offer result = service.findOfferByOfferId(offerId);

        // then
        assertEquals(offer, result);
    }

    @DisplayName("존재하지 않는 ID가 주어진다면_제안을 조회할 때_예외가 발생해야 한다.")
    @Test
    void givenNonExistingId_whenFindOffer_thenThrowsException() {
        // given
        when(repository.findById(offerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findOfferByOfferId(offerId));
    }
}