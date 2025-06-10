package xyz.samsami.blokey_land.offer.mapper;

import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.offer.domain.Offer;
import xyz.samsami.blokey_land.offer.dto.OfferReqCreateDto;
import xyz.samsami.blokey_land.user.domain.User;

public class OfferMapper {
    public static Offer toEntity(OfferReqCreateDto dto, Project project, User user) {
        return Offer.builder()
            .project(project)
            .user(user)
            .proposer(dto.getProposer())
            .status(dto.getStatus())
            .build();
    }
}