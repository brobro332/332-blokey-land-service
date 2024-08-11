package kr.co.co_working.project.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -1778419544L;

    public static final QProject project = new QProject("project");

    public final kr.co.co_working.common.entity.QCommonTime _super = new kr.co.co_working.common.entity.QCommonTime(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final ListPath<kr.co.co_working.task.repository.entity.Task, kr.co.co_working.task.repository.entity.QTask> tasks = this.<kr.co.co_working.task.repository.entity.Task, kr.co.co_working.task.repository.entity.QTask>createList("tasks", kr.co.co_working.task.repository.entity.Task.class, kr.co.co_working.task.repository.entity.QTask.class, PathInits.DIRECT2);

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}

