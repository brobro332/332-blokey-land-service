package xyz.samsami.blokey_land.offer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqReadDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.mapper.OfferMapper;
import xyz.samsami.blokey_land.offer.repository.OfferRepository;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;
import xyz.samsami.blokey_land.user.domain.User;
import xyz.samsami.blokey_land.user.service.UserService;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfferService {
    private final ProjectService projectService;
    private final UserService userService;
    private final OfferRepository repository;

    @Transactional
    public void createOffer(Long projectId, UUID userId, OfferReqCreateDto dto) {
        Project project = projectService.findProjectByProjectId(projectId);
        User user = userService.findUserByUserId(userId);
        if (project != null && user != null) repository.save(OfferMapper.toEntity(dto, project, user));
    }

    public Page<OfferRespDto> readOffers(OfferReqReadDto dto, Pageable pageable) {
        Long projectId = dto.getProjectId();
        UUID userId = dto.getUserId();

        if (projectId != null) return repository.findDtoByProjectId(projectId, pageable);
        if (userId != null) return repository.findDtoByUserId(userId, pageable);

        throw new CommonException(ExceptionType.BAD_REQUEST, null);
    }

    @Transactional
    public void updateOffer(Long offerId, OfferReqUpdateDto dto) {
        Offer offer = findOfferByOfferId(offerId);
        if (offer != null) offer.updateStatus(dto.getStatus());
    }

    @Transactional
    public void deleteOffer(Long offerId) {
        Offer offer = findOfferByOfferId(offerId);
        if (offer != null) repository.delete(offer);
    }

    public Offer findOfferByOfferId(Long offerId) {
        return repository.findById(offerId).orElseThrow(() ->
            new CommonException(ExceptionType.NOT_FOUND, null)
        );
    }
}