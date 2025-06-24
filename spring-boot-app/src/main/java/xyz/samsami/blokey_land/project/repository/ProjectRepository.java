package xyz.samsami.blokey_land.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.samsami.blokey_land.project.domain.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}