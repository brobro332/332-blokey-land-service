package xyz.samsami.blokey_land.task.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import xyz.samsami.blokey_land.task.domain.Task;
import xyz.samsami.blokey_land.task.dto.TaskRespDto;
import xyz.samsami.blokey_land.task.mapper.TaskMapper;

import java.util.List;

import static xyz.samsami.blokey_land.task.domain.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskDslRepository {

    private final JPAQueryFactory queryFactory;

    public Page<TaskRespDto> readTasksByProjectId(Long projectId, Pageable pageable) {
        List<Task> tasks = fetchTasks(projectId, pageable);
        long total = fetchTotalCount(projectId);

        List<TaskRespDto> content = tasks.stream()
            .map(TaskMapper::toRespDto)
            .toList();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanBuilder buildPredicate(Long projectId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(task.project.id.eq(projectId));

        return builder;
    }

    private List<Task> fetchTasks(Long projectId, Pageable pageable) {
        BooleanBuilder predicate = buildPredicate(projectId);

        JPAQuery<Task> query = queryFactory.select(task)
            .from(task)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(task.id.desc());

        return query.fetch();
    }

    private long fetchTotalCount(Long projectId) {
        BooleanBuilder predicate = buildPredicate(projectId);

        Long total = queryFactory.select(task.count())
            .from(task)
            .where(predicate)
            .fetchOne();

        return total != null ? total : 0;
    }
}

