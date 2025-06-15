package xyz.samsami.blokey_land.offer.mapper;

import xyz.samsami.blokey_land.blokey.domain.Blokey;
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
}