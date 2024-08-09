package kr.co.co_working.task.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.co_working.task.dto.QTaskResponseDto;
import kr.co.co_working.task.dto.TaskRequestDto;
import kr.co.co_working.task.dto.TaskResponseDto;
import kr.co.co_working.task.repository.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.co_working.project.repository.entity.QProject.project;
import static kr.co.co_working.task.repository.entity.QTask.task;


@Repository
@RequiredArgsConstructor
public class TaskDslRepositoryImpl implements TaskDslRepository {
    private final JPAQueryFactory factory;

    /**
     * SELECT task_id, task_name, task_type, task_description, task_createdAt, task_modifiedAt
     * FROM tbl_task
     * WHERE task_name = ?
     * AND task_type = ?
     * AND task_description = ?;
     *
     * @param dto
     * @return
     */
    @Override
    public List<TaskResponseDto> readTaskList(TaskRequestDto.READ dto) {
        return factory
                .select(new QTaskResponseDto(task.id, task.name, task.type, task.description, task.createdAt, task.modifiedAt))
                .from(task)
                .where(   nameEq(dto.getName())
                        , typeEq(dto.getType())
                        , descriptionEq(dto.getDescription()) )
                .fetch();
    }

    /**
     * SELECT *
     * FROM tbl_task T
     * [INNER] JOIN tbl_project P
     * WHERE T.project_id = P.project_id;
     *
     * @param id
     * @return
     */
    @Override
    public List<Task> selectTaskListByProject(Long id) {
        return factory
                .select(task)
                .from(task)
                .join(task.project, project)
                .where(projectIdEq(id))
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

    private BooleanExpression projectIdEq(Long idCond) {
        return idCond != null ? task.project.id.eq(idCond) : null;
    }
}
