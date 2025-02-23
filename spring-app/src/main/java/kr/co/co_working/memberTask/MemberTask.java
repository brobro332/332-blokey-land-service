package kr.co.co_working.memberTask;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.member.Member;
import kr.co.co_working.task.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_member_task")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTask extends CommonTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_task_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Builder
    public MemberTask(Member member, Task task) {
        this.member = member;
        this.task = task;
    }

    public void updateMemberTask(Member member, Task task) {
        this.member = member;
        this.task = task;
    }
}