package xyz.samsami.blokey_land.offer.dto;

import lombok.*;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferRespDto {
    private Long offerId;
    private Long projectId;
    private UUID userId;
    private OfferType proposer;
    private OfferStatusType status;
}