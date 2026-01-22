package com.slog.blog_api.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -984513978L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.slog.blog_api.global.entity.QBaseTimeEntity _super = new com.slog.blog_api.global.entity.QBaseTimeEntity(this);

    public final com.slog.blog_api.domain.category.entity.QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.slog.blog_api.domain.tag.entity.PostTag, com.slog.blog_api.domain.tag.entity.QPostTag> postTags = this.<com.slog.blog_api.domain.tag.entity.PostTag, com.slog.blog_api.domain.tag.entity.QPostTag>createList("postTags", com.slog.blog_api.domain.tag.entity.PostTag.class, com.slog.blog_api.domain.tag.entity.QPostTag.class, PathInits.DIRECT2);

    public final EnumPath<PostStatus> status = createEnum("status", PostStatus.class);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.slog.blog_api.domain.category.entity.QCategory(forProperty("category")) : null;
    }

}

