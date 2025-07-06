package xyz.samsami.blokey_land.offer.dto;

import lombok.*;
import xyz.samsami.blokey_land.offer.type.OfferType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferReqReadDto {
    private Long projectId;
    private UUID blokeyId;
    private OfferType offerer;
}