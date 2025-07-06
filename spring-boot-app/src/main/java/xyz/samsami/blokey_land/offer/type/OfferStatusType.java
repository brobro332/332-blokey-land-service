package xyz.samsami.blokey_land.offer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OfferStatusType {
    PENDING("대기중"),
    ACCEPTED("승낙"),
    REJECTED("거절");

    private final String description;
}