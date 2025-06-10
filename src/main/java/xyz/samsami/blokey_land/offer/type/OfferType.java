package xyz.samsami.blokey_land.offer.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OfferType {
    PROJECT("프로젝트"),
    USER("사용자");

    private final String description;
}