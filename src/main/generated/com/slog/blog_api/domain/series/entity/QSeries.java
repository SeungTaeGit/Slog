package com.slog.blog_api.domain.series.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSeries is a Querydsl query type for Series
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSeries extends EntityPathBase<Series> {

    private static final long serialVersionUID = -1406500364L;

    public static final QSeries series = new QSeries("series");

    public final com.slog.blog_api.global.entity.QBaseTimeEntity _super = new com.slog.blog_api.global.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSeries(String variable) {
        super(Series.class, forVariable(variable));
    }

    public QSeries(Path<? extends Series> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSeries(PathMetadata metadata) {
        super(Series.class, metadata);
    }

}

