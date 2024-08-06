package kr.co.co_working.project.service;

import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.repository.ProjectRepository;
import kr.co.co_working.project.repository.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository repository;

    /**
     * createProject : Project 등록
     * @param dto
     * @throws Exception
     */
    public void createProject(ProjectRequestDto.CREATE dto) throws Exception {
        try {
            // 1. Project 빌드
            Project project = Project.builder()
                    .name(dto.getName())
                    .description(dto.getDescription())
                    .build();

            // 2. 등록
            repository.save(project);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}