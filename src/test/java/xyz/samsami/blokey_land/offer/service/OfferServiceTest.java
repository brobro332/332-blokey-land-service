package xyz.samsami.blokey_land.offer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqReadDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.repository.OfferRepository;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    @InjectMocks
    OfferService service;

    @Mock
    private ProjectService projectService;

    @Mock
    private MemberService memberService;

    @Mock
    private BlokeyService blokeyService;

    @Mock
    private OfferRepository repository;

    @Test
    void createOffer_validInput_true() {
        // given
        Long projectId = 1L;
        UUID blokeyId = UUID.randomUUID();
        OfferReqCreateDto dto = new OfferReqCreateDto();

        Project project = mock(Project.class);
        Blokey blokey = mock(Blokey.class);
        Offer offer = mock(Offer.class);

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);
        when(blokeyService.findBlokeyByBlokeyId(blokeyId)).thenReturn(blokey);
        when(repository.save(any(Offer.class))).thenReturn(offer);

        // when
        service.createOffer(projectId, blokeyId, dto);

        // then
        verify(repository).save(any(Offer.class));
    }

    @Test
    void readOffers_byProjectId_success() {
        // given
        Long projectId = 1L;
        OfferReqReadDto dto = OfferReqReadDto.builder()
            .projectId(projectId)
            .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<OfferRespDto> page = Page.empty();

        when(repository.findDtoByProjectId(projectId, pageable)).thenReturn(page);

        // when
        Page<OfferRespDto> result = service.readOffers(dto, pageable);

        // then
        assertEquals(page, result);
    }

    @Test
    void readOffers_byBlokeyId_success() {
        // given
        UUID blokeyId = UUID.randomUUID();
        OfferReqReadDto dto = OfferReqReadDto.builder()
            .blokeyId(blokeyId)
            .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<OfferRespDto> page = Page.empty();

        when(repository.findDtoByBlokeyId(blokeyId, pageable)).thenReturn(page);

        // when
        Page<OfferRespDto> result = service.readOffers(dto, pageable);

        // then
        assertEquals(page, result);
    }

    @Test
    void readOffers_invalidInput_false() {
        // given
        OfferReqReadDto dto = new OfferReqReadDto();
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        assertThrows(CommonException.class, () ->
            service.readOffers(dto, pageable)
        );
    }

    @Test
    void updateOffer_statusAccepted_success() {
        // given
        Long offerId = 1L;
        Long projectId = 10L;
        UUID blokeyId = UUID.randomUUID();

        OfferReqUpdateDto dto = new OfferReqUpdateDto(OfferStatusType.ACCEPTED);

        Project project = Project.builder().id(projectId).build();
        Blokey blokey = Blokey.builder().id(blokeyId).build();

        Offer offer = mock(Offer.class);
        when(offer.getProject()).thenReturn(project);
        when(offer.getBlokey()).thenReturn(blokey);

        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        service.updateOffer(offerId, dto);

        // then
        verify(offer).updateStatus(OfferStatusType.ACCEPTED);
        verify(memberService).createMember(project, blokey);
    }

    @Test
    void deleteOffer_validInput_true() {
        // given
        Long offerId = 1L;
        Offer offer = mock(Offer.class);

        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        service.deleteOffer(offerId);

        // then
        verify(repository).delete(offer);
    }
}