package kr.co.co_working.repository.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tbl_task")
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_name", nullable = false, length = 20)
    private String name;

    @Column(name = "task_type", nullable = false, length = 10)
    private String type;

    @Column(name = "task_description", nullable = false, length = 200)
    private String description;

    @Builder
    public Task(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }
}