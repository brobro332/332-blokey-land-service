package xyz.samsami.blokey_land.skill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.blokey.domain.Blokey;
import xyz.samsami.blokey_land.skill.domain.BlokeySkill;
import xyz.samsami.blokey_land.skill.domain.Skill;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface BlokeySkillRepository extends JpaRepository<BlokeySkill, Long> {
    BlokeySkill findByBlokeyAndSkill(Blokey blokey, Skill skill);
    List<BlokeySkill> findByBlokeyIdIn(Set<UUID> blokeyIds);
}
