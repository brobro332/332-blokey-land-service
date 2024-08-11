package kr.co.co_working.project.service;

import kr.co.co_working.project.dto.ProjectRequestDto;
import kr.co.co_working.project.dto.ProjectResponseDto;
import kr.co.co_working.project.repository.ProjectDslRepository;
import kr.co.co_working.project.repository.ProjectRepository;
import kr.co.co_working.project.repository.entity.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository repository;
    private final ProjectDslRepository dslRepository;

    /**
     * createProject : Project 등록
     * @param dto
     * @throws Exception
     */
    @Transactional
    public Long createProject(ProjectRequestDto.CREATE dto) throws Exception {
        // 1. Project 빌드
        Project project = Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .tasks(new ArrayList<>())
                .build();

        // 2. 등록
        repository.save(project);

        // 3. ID 반환
        return project.getId();
    }

    /**
     * readProjectList : Project 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<ProjectResponseDto> readProjectList(ProjectRequestDto.READ dto) throws Exception {
        // QueryDSL 동적 쿼리 결과 반환
        return dslRepository.readProjectList(dto);
    }

    /**
     * updateProject : Project 수정
     * @param id
     * @param dto
     * @throws Exception
     */
    public void updateProject(Long id, ProjectRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. ID에 해당하는 프로젝트 조회
        Optional<Project> selectedProject = repository.findById(id);

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("수정하려는 프로젝트가 존재하지 않습니다. ID : " + id);
        }

        // 3. 프로젝트 수정사항 처리
        Project project = selectedProject.get();
        project.updateProject(dto.getName(), dto.getDescription());
    }

    /**
     * deleteProject : Project 삭제
     * @param id
     * @throws Exception
     */
    public void deleteProject(Long id) throws NoSuchElementException, Exception {
        // 1. ID에 해당하는 프로젝트 조회
        Optional<Project> selectedProject = repository.findById(id);

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("삭제하려는 프로젝트가 존재하지 않습니다. ID : " + id);
        }

        // 3. 존재 시 삭제 처리
        repository.delete(selectedProject.get());
    }
}