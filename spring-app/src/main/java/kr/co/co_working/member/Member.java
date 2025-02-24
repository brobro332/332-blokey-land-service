package kr.co.co_working.member;

import jakarta.persistence.*;
import kr.co.co_working.common.CommonTime;
import kr.co.co_working.memberTask.MemberTask;
import kr.co.co_working.memberWorkspace.MemberWorkspace;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "tbl_member")
public class Member extends CommonTime {
    @Id
    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_password", nullable = false, length = 100)
    private String password;

    @Column(name = "member_name", nullable = false, length = 20)
    private String name;

    @Column(name = "member_description", length = 200)
    private String description;

    @Column(name = "member_del_flag", nullable = false, length = 1)
    private String delFlag;

    @PrePersist
    public void prePersist() {
        if (this.delFlag == null) {
            this.delFlag = "0";
        }
    }

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberWorkspace> memberWorkspaces = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "member")
    private List<MemberTask> memberTasks = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, String description) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.description = description;
    }

    public void updateMember(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateDelFlag(String delFlag) { this.delFlag = delFlag; }
}