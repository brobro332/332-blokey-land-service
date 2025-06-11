package xyz.samsami.blokey_land.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferReqCreateDto {
    private OfferType offerer;
    private OfferStatusType status;
}