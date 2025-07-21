package xyz.samsami.blokey_land.skill.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import xyz.samsami.blokey_land.skill.domain.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
        @Modifying
        @Transactional
        @Query(value = """
                INSERT INTO skill (name, display_name)
                VALUES (:name, :displayName)
                ON CONFLICT (name) DO NOTHING
        """, nativeQuery = true)
        void insertIgnoreConflict(@Param("name") String name, @Param("displayName") String displayName);
}
