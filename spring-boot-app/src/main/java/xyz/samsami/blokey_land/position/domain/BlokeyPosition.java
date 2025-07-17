package xyz.samsami.blokey_land.position.domain;

import jakarta.persistence.*;
import lombok.*;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BlokeyPosition extends CommonTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Blokey blokey;

    @ManyToOne
    private Position position;

    public void updateBlokey(Blokey blokey) { if (blokey != null) this.blokey = blokey; }
}
