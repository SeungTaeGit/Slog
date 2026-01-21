package com.slog.blog_api.domain.post.repository;

import com.slog.blog_api.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.slog.blog_api.domain.post.entity.QPost.post;
import static com.slog.blog_api.domain.category.entity.QCategory.category;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> search(String keyword, String categoryName, Pageable pageable) {

        List<Post> content = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.category, category).fetchJoin()
                .where(
                        containsKeyword(keyword),
                        eqCategory(categoryName)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(
                        containsKeyword(keyword),
                        eqCategory(categoryName)
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> count == null ? 0 : count);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return post.title.contains(keyword).or(post.content.contains(keyword));
    }

    private BooleanExpression eqCategory(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            return null;
        }
        return category.name.eq(categoryName);
    }
}