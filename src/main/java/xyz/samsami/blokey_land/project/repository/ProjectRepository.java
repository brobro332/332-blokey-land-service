package xyz.samsami.blokey_land.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.project.domain.Project;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findOptionalById(Long projectId);
    Page<Project> findPageAll(Pageable pageable);
}