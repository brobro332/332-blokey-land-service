package xyz.samsami.blokey_land.offer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.blokey.service.BlokeyService;
import xyz.samsami.blokey_land.common.exception.CommonException;
import xyz.samsami.blokey_land.common.type.ExceptionType;
import xyz.samsami.blokey_land.member.service.MemberService;
import xyz.samsami.blokey_land.member.type.RoleType;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqReadDto;
import xyz.samsami.blokey_land.offer.dto.OfferReqUpdateDto;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.offer.mapper.OfferMapper;
import xyz.samsami.blokey_land.offer.repository.OfferRepository;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.project.service.ProjectService;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfferService {
    private final ProjectService projectService;
    private final BlokeyService blokeyService;
    private final MemberService memberService;
    private final OfferRepository repository;

    @Transactional
    public OfferRespDto createOffer(OfferReqCreateDto dto) {
        Project project = projectService.findProjectByProjectId(dto.getProjectId());
        Blokey blokey = blokeyService.findBlokeyByBlokeyId(dto.getBlokeyId());
        if (project != null && blokey != null) return OfferMapper.toRespDto(repository.save(OfferMapper.toEntity(dto, project, blokey)));

        return null;
    }

    public Page<OfferRespDto> readOffers(OfferReqReadDto dto, Pageable pageable) {
        Long projectId = dto.getProjectId();
        UUID blokeyId = dto.getBlokeyId();
        OfferType offerer = dto.getOfferer();

        if (projectId != null) return repository.findDtoByProjectId(projectId, offerer, pageable);
        if (blokeyId != null) return repository.findDtoByBlokeyId(blokeyId, offerer, pageable);

        throw new CommonException(ExceptionType.BAD_REQUEST, null);
    }

    @Transactional
    public void updateOfferByOfferId(Long offerId, OfferReqUpdateDto dto) {
        Offer offer = findOfferByOfferId(offerId);
        if (offer != null) {
            OfferStatusType status = dto.getStatus();
            offer.updateStatus(status);

            if (OfferStatusType.ACCEPTED.equals(status)) memberService.createMember(offer.getProject(), offer.getBlokey(), RoleType.MEMBER);
        }
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