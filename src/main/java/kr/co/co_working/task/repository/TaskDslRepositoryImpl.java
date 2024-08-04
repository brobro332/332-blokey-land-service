package kr.co.co_working.task.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.task.dto.QTaskResponseDto;
import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.task.repository.entity.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskDslRepositoryImpl implements TaskDslRepository {
    private final JPAQueryFactory factory;

    @Override
    public List<TaskResponseDto> selectTaskList(TaskRequestDto.READ dto) {
        return factory
                .select(new QTaskResponseDto(task.id, task.name, task.type, task.description, task.createdAt, task.modifiedAt))
                .from(task)
                .where(   nameEq(dto.getName())
                        , typeEq(dto.getType())
                        , descriptionEq(dto.getDescription()) )
                .fetch();
    }

    private BooleanExpression nameEq(String nameCond) {
        return nameCond != null ? task.name.eq(nameCond) : null;
    }

    private BooleanExpression typeEq(String typeCond) {
        return typeCond != null ? task.type.eq(typeCond) : null;
    }

    private BooleanExpression descriptionEq(String descriptionCond) {
        return descriptionCond != null ? task.description.eq(descriptionCond) : null;
    }
}
