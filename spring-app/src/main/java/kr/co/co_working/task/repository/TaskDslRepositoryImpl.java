package kr.co.co_working.task.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.task.dto.QTaskResponseDto;
import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static kr.co.co_working.task.QTask.task;


@Repository
@RequiredArgsConstructor
public class TaskDslRepositoryImpl implements TaskDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT task_id, task_name, task_type, task_description, task_createdAt, task_modifiedAt
     * FROM tbl_task
     * WHERE task_name LIKE ?
     * AND task_type LIKE ?
     * AND task_description LIKE ?
     * AND ( task_start_at <= ?
     *       OR task_end_at >= ? );
     *
     * @param dto
     * @return
     */
    @Override
    public List<TaskResponseDto> readTaskList(TaskRequestDto.READ dto) {
        BooleanBuilder      booleanBuilder    = new BooleanBuilder();
        LocalDateTime       startAt     = dto.getStartAt();
        LocalDateTime       endAt       = dto.getEndAt();

        if (startAt != null) {
            booleanBuilder.or(task.endAt.goe(startAt));
        }
        if (endAt != null) {
            booleanBuilder.or(task.startAt.loe(endAt));
        }

        return factory
                .select(new QTaskResponseDto(task.id, task.name, task.type, task.description, task.createdAt, task.modifiedAt))
                .from(task)
                .where(   nameContains(dto.getName())
                        , typeContains(dto.getType())
                        , descriptionContains(dto.getDescription())
                        , booleanBuilder)
                .fetch();
    }

    private BooleanExpression nameContains(String nameCond) {
        return nameCond != null ? task.name.contains(nameCond) : null;
    }

    private BooleanExpression typeContains(String typeCond) {
        return typeCond != null ? task.type.contains(typeCond) : null;
    }

    private BooleanExpression descriptionContains(String descriptionCond) {
        return descriptionCond != null ? task.description.contains(descriptionCond) : null;
    }
}
