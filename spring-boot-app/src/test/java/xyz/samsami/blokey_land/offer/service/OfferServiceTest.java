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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    @InjectMocks private OfferService service;
    @Mock private BlokeyService blokeyService;
    @Mock private ProjectService projectService;
    @Mock private MemberService memberService;
    @Mock private OfferRepository repository;

    private Offer offer;
    private UUID blokeyId;
    private Blokey blokey;
    private Long projectId;
    private Project project;

    @BeforeEach
    void setUp() {
        offer = mock(Offer.class);

        blokeyId = UUID.randomUUID();
        blokey = new Blokey(blokeyId, "nickname", "bio");

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

    @DisplayName("Offer를 저장할 때 모든 필수 값이 저장되어야 한다.")
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

    @DisplayName("유효한 파라미터가 주어졌을 때 Offer 상태를 수정해야 한다.")
    @Test
    void givenValidParameter_whenUpdateOffer_thenStatusUpdatedAndMemberCreatedIfAccepted() {
        // given
        Long offerId = 1L;
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

    @DisplayName("상태가 ACCEPTED가 아닐 경우 Member 생성 호출은 없어야 한다.")
    @Test
    void givenNotAcceptedStatus_whenUpdateOffer_thenOnlyStatusUpdated() {
        // given
        Long offerId = 1L;
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

    @DisplayName("유효한 ID가 주어졌을 때 Offer를 삭제해야 한다.")
    @Test
    void givenValidOfferId_whenDeleteOffer_thenOfferDeleted() {
        // given
        Long offerId = 1L;

        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        service.deleteOffer(offerId);

        // then
        verify(repository).delete(offer);
    }

    @DisplayName("존재하는 ID가 주어졌을 때 객체를 반환해야 한다.")
    @Test
    void givenValidOfferId_whenFindOffer_thenReturnOffer() {
        // given
        Long offerId = 1L;

        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        Offer found = service.findOfferByOfferId(offerId);

        // then
        assertEquals(offer, found);
    }

    @DisplayName("존재하지 않는 ID 조회 시 예외가 발생해야 한다.")
    @Test
    void givenInvalidOfferId_whenFindOffer_thenThrowCommonException() {
        // given
        Long offerId = 1L;

        when(repository.findById(offerId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> service.findOfferByOfferId(offerId));
    }
}