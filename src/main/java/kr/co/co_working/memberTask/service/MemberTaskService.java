package kr.co.co_working.memberTask.service;

import kr.co.co_working.member.Member;
import kr.co.co_working.memberTask.MemberTask;
import kr.co.co_working.memberTask.repository.MemberTaskRepository;
import kr.co.co_working.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberTaskService {
    private final MemberTaskRepository repository;

    /**
     * createMemberTask : MemberTask 등록
     * @param member
     * @param task
     * @return
     * @throws NoSuchElementException
     * @throws Exception
     */
    public Long createMemberTask(Member member, Task task) throws Exception {
        // 1. MemberTask 빌드
        MemberTask memberTask = MemberTask.builder()
                .member(member)
                .task(task)
                .build();

        // 2. MemberTask 등록
        repository.save(memberTask);

        // 3. ID 반환
        return memberTask.getId();
    }

    /**
     * deleteMemberTask : MemberTask 삭제
     * @param task
     * @throws Exception
     */
    public void deleteMemberTask(Task task) throws Exception {
        // 1. MemberTask 조회
        List<MemberTask> memberTasks = repository.findByTaskId(task.getId());

        // 2. MemberTask 삭제
        for (MemberTask memberTask : memberTasks) {
            repository.delete(memberTask);
        }
    }
}