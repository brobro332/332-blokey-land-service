package kr.co.co_working.workspace.repository;

import kr.co.co_working.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
