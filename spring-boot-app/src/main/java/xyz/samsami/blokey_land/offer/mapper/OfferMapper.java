package xyz.samsami.blokey_land.offer.mapper;

import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.offer.dto.OfferRespDto;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;

public class OfferMapper {
    public static Offer toEntity(OfferReqCreateDto dto, Project project, Blokey blokey) {
        return Offer.builder()
            .project(project)
            .blokey(blokey)
            .offerer(dto.getOfferer())
            .status(dto.getStatus())
            .build();
    }

    public static OfferRespDto toRespDto(Offer offer) {
        return OfferRespDto.builder()
            .id(offer.getId())
            .projectId(offer.getProject().getId())
            .projectTitle(offer.getProject().getTitle())
            .blokeyId(offer.getBlokey().getId())
            .offerer(offer.getOfferer())
            .status(offer.getStatus())
            .createdAt(offer.getCreatedAt())
            .build();
    }
}