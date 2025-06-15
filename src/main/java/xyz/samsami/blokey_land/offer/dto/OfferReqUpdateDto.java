package xyz.samsami.blokey_land.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferReqUpdateDto {
    private OfferStatusType status;
}