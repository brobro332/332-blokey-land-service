package kr.co.co_working.task.service;

import kr.co.co_working.member.Member;
import kr.co.co_working.member.repository.MemberRepository;
import kr.co.co_working.memberTask.service.MemberTaskService;
import kr.co.co_working.project.repository.ProjectRepository;
import kr.co.co_working.project.Project;
import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;
import kr.co.co_working.task.repository.TaskDslRepository;
import kr.co.co_working.task.repository.TaskRepository;
import kr.co.co_working.task.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {
    private final MemberTaskService memberTaskService;
    private final TaskRepository repository;
    private final TaskDslRepository dslRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    /**
     * createTask : Task 등록
     * @param dto
     * @throws Exception
     */
    public Long createTask(TaskRequestDto.CREATE dto) throws NoSuchElementException, Exception {
        // 1. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("등록하려는 업무에 해당하는 프로젝트가 존재하지 않습니다. ID : " + dto.getProjectId());
        }
        Project project = selectedProject.get();

        // 3. Task 빌드
        Task task = Task.builder()
                .name(dto.getName())
                .type(dto.getType())
                .description(dto.getDescription())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .build();

        // 4. Member 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<Member> selectedMember = memberRepository.findById(email);

        // 5. Member 추출
        Member member = selectedMember.get();

        // 6. 등록
        repository.save(task);
        memberTaskService.createMemberTask(member, task);
        project.insertTask(task);

        // 7. ID 반환
        return task.getId();
    }

    /**
     * selectTaskList : Task 조회
     * @param dto
     * @return
     * @throws Exception
     */
    public List<TaskResponseDto> readTaskList(TaskRequestDto.READ dto) throws Exception {
        // QueryDSL 동적 쿼리 결과 반환
        return dslRepository.readTaskList(dto);
    }

    /**
     * updateTask : Task 수정
     * @param id
     * @param dto
     * @throws Exception
     */
    @Transactional
    public void updateTask(Long id, TaskRequestDto.UPDATE dto) throws NoSuchElementException, Exception {
        // 1. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("수정하려는 업무에 해당하는 프로젝트가 존재하지 않습니다. ID : " + dto.getProjectId());
        }

        // 3. ID에 해당하는 업무 조회
        Optional<Task> selectedTask = repository.findById(id);

        // 4. 부재 시 예외 처리
        if (selectedTask.isEmpty()) {
            throw new NoSuchElementException("수정하려는 업무가 존재하지 않습니다. ID" + id);
        }

        // 5. 존재 시 수정 처리
        Task task = selectedTask.get();
            task.updateTask(dto.getName(), dto.getType(), dto.getDescription(), task.getProject(), dto.getStartAt(), dto.getEndAt());
    }

    /**
     * deleteTask : Task 삭제
     * @param id
     * @param dto
     * @throws Exception
     */
    @Transactional
    public void deleteTask(Long id, TaskRequestDto.DELETE dto) throws NoSuchElementException, Exception {
        // 1. Project 조회
        Optional<Project> selectedProject = projectRepository.findById(dto.getProjectId());

        // 2. 부재 시 예외 처리
        if (selectedProject.isEmpty()) {
            throw new NoSuchElementException("수정하려는 업무에 해당하는 프로젝트가 존재하지 않습니다. ID : " + dto.getProjectId());
        }
        Project project = selectedProject.get();

        // 3. ID에 해당하는 업무 조회
        Optional<Task> selectedTask = repository.findById(id);

        // 4. 부재 시 예외 처리
        if (selectedTask.isEmpty()) {
            throw new NoSuchElementException("삭제하려는 업무가 존재하지 않습니다. ID : " + id);
        }
        Task task = selectedTask.get();

        // 5. 존재 시 삭제 처리
        memberTaskService.deleteMemberTask(task);
        repository.delete(task);
        project.deleteTask(task);
    }
}