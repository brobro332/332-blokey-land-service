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
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqReadDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.repository.OfferRepository;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.service.UserService;

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
    private UserService userService;

    @Mock
    private OfferRepository repository;

    @Test
    void createOffer_validInput_true() {
        // given
        Long projectId = 1L;
        UUID userId = UUID.randomUUID();
        OfferReqCreateDto dto = new OfferReqCreateDto();

        Project project = mock(Project.class);
        User user = mock(User.class);
        Offer offer = mock(Offer.class);

        when(projectService.findProjectByProjectId(projectId)).thenReturn(project);
        when(userService.findUserByUserId(userId)).thenReturn(user);
        when(repository.save(any(Offer.class))).thenReturn(offer);

        // when
        service.createOffer(projectId, userId, dto);

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
    void readOffers_byUserId_success() {
        // given
        UUID userId = UUID.randomUUID();
        OfferReqReadDto dto = OfferReqReadDto.builder()
            .userId(userId)
            .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<OfferRespDto> page = Page.empty();

        when(repository.findDtoByUserId(userId, pageable)).thenReturn(page);

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
    void updateOffer_validInput_true() {
        // given
        Long offerId = 1L;
        OfferReqUpdateDto dto = OfferReqUpdateDto.builder()
            .status(OfferStatusType.ACCEPTED)
            .build();

        Offer offer = mock(Offer.class);
        when(repository.findById(offerId)).thenReturn(Optional.of(offer));

        // when
        service.updateOffer(offerId, dto);

        // then
        verify(offer).updateStatus(OfferStatusType.ACCEPTED);
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