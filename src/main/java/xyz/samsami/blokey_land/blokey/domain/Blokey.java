package xyz.samsami.blokey_land.blokey.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import xyz.samsami.blokey_land.common.domain.CommonTimestamp;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Blokey extends CommonTimestamp {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(length = 1000)
    private String bio;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean deleted = false;

    public void updateNickname(String nickname) { if (nickname != null) this.nickname = nickname; }
    public void updateBio(String bio) { if (bio != null) this.bio = bio; }
    public void updateDeleted(Boolean deleted) { if (deleted != null) this.deleted = deleted; }
}