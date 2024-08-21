package kr.co.co_working.project.repository;

import kr.co.co_working.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}