package kr.co.co_working.common.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommonTime is a Querydsl query type for CommonTime
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QCommonTime extends EntityPathBase<CommonTime> {

    private static final long serialVersionUID = 1094197397L;

    public static final QCommonTime commonTime = new QCommonTime("commonTime");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modifiedAt = createDateTime("modifiedAt", java.time.LocalDateTime.class);

    public QCommonTime(String variable) {
        super(CommonTime.class, forVariable(variable));
    }

    public QCommonTime(Path<? extends CommonTime> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommonTime(PathMetadata metadata) {
        super(CommonTime.class, metadata);
    }

}

