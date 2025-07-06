package xyz.samsami.blokey_land.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferRespDto {
    private Long id;
    private Long projectId;
    private String projectTitle;
    private UUID blokeyId;
    private String blokeyNickname;
    private String blokeyBio;
    private OfferType offerer;
    private OfferStatusType status;
    private LocalDateTime createdAt;
}