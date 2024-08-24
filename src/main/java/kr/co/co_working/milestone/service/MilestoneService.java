package kr.co.co_working.milestone.service;

import kr.co.co_working.milestone.Milestone;
import kr.co.co_working.milestone.dto.MilestoneRequestDto;
import kr.co.co_working.milestone.dto.MilestoneResponseDto;
import kr.co.co_working.milestone.repository.MilestoneDslRepository;
import kr.co.co_working.milestone.repository.MilestoneRepository;
import kr.co.co_working.project.Project;
import kr.co.co_working.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MilestoneService {
    private final MilestoneRepository repository;
    private final ProjectRepository projectRepository;
    private final MilestoneDslRepository dslRepository;

    /**
     * createMilestone : Milestone 등록
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    public void createMilestone(MilestoneRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("등록하려는 프로젝트가 존재하지 않습니다.");
        }

        // 3. Milestone 빌드
        Milestone milestone = Milestone.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .dueAt(dto.getDueAt())
                .build();

        // 4. Milestone 등록
        repository.save(milestone);
    }

    /**
     * readMilestoneList : Milestone 조회
     * @param dto
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public List<MilestoneResponseDto> readMilestone(MilestoneRequestDto.READ dto) throws NoSuchElementException, Exception {
        // 1. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("조회하려는 프로젝트가 존재하지 않습니다.");
        }

        // 3. Milestone 조회 및 반환
        return dslRepository.readMilestoneList(dto);
    }

    /**
     * updateMilestone : Milestone 수정
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void updateMilestone(MilestoneRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. Milestone 조회
        Optional<Milestone> selectedMilestone = repository.findById(dto.getId());

        // 2. 부재 시 예외 처리
        if (selectedMilestone.isEmpty()) {
            throw new NoSuchElementException("수정하려는 마일스톤이 존재하지 않습니다.");
        }

        // 3. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 4. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("수정하려는 프로젝트가 존재하지 않습니다.");
        }

        // 5. Milestone, Project 객체 추출
        Milestone milestone = selectedMilestone.get();
        Project project = selectedProject.get();

        // 6. Milestone 수정
        milestone.updateMilestone(dto.getName(), dto.getDescription(), dto.getDueAt(), project);
    }

    /**
     * deleteMilestone : Milestone 삭제
     * @param dto
     * @throws NoSuchElementException
     * @throws Exception
     */
    @Transactional
    public void deleteMilestone(MilestoneRequestDto.DELETE dto) throws NoSuchElementException, Exception {
        // 1. Milestone 조회
        Optional<Milestone> selectedMilestone = repository.findById(dto.getId());

        // 2. 부재 시 예외 처리
        if (selectedMilestone.isEmpty()) {
            throw new NoSuchElementException("수정하려는 마일스톤이 존재하지 않습니다.");
        }

        // 3. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 4. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("수정하려는 프로젝트가 존재하지 않습니다.");
        }

        // 5. Milestone 객체 추출
        Milestone milestone = selectedMilestone.get();

        // 6. Milestone 삭제
        repository.delete(milestone);
    }
}