package xyz.samsami.blokey_land.skill.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Skill extends CommonTimestamp {
    @Id
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;
}
