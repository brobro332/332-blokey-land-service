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
    private UUID blokeyId;
    private OfferType offerer;
    private OfferStatusType status;
}