package xyz.samsami.blokey_land.offer.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;
import xyz.samsami.blokey_land.project.domain.Project;
import xyz.samsami.blokey_land.offer.type.OfferStatusType;
import xyz.samsami.blokey_land.offer.type.OfferType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Offer extends CommonTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blokey_id")
    private Blokey blokey;

    @Enumerated(EnumType.STRING)
    private OfferType offerer;

    @Enumerated(EnumType.STRING)
    private OfferStatusType status;

    public void updateStatus(OfferStatusType status) { if (status != null) this.status = status; }
}