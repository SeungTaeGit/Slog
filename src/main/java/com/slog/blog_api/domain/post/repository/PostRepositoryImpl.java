package com.slog.blog_api.domain.post.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.dto.PostSearchCondition;
import com.slog.blog_api.domain.post.dto.SidebarDto;
import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.domain.post.entity.PostStatus;

import static com.slog.blog_api.domain.post.entity.QPost.post;
import static com.slog.blog_api.domain.category.entity.QCategory.category;
import static com.slog.blog_api.domain.series.entity.QSeries.series;
import static com.slog.blog_api.domain.tag.entity.QTag.tag;
import static com.slog.blog_api.domain.tag.entity.QPostTag.postTag;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostResponse> search(PostSearchCondition condition, Pageable pageable) {

        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.category, category).fetchJoin()
                .leftJoin(post.series, series).fetchJoin()
                .leftJoin(postTag).on(postTag.post.eq(post))
                .leftJoin(postTag.tag, tag)
                .where(
                        isPublic(),
                        keywordContains(condition.getKeyword()),
                        categoryEq(condition.getCategoryName()),
                        seriesEq(condition.getSeriesName()),
                        tagEq(condition.getTagName())
                )
                .groupBy(post.id);

        applySorting(query, pageable);

        List<Post> posts = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(postTag).on(postTag.post.eq(post))
                .leftJoin(postTag.tag, tag)
                .where(
                        isPublic(),
                        keywordContains(condition.getKeyword()),
                        categoryEq(condition.getCategoryName()),
                        seriesEq(condition.getSeriesName()),
                        tagEq(condition.getTagName())
                )
                .fetchOne();

        List<PostResponse> content = posts.stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public List<SidebarDto.CategoryCount> getCategoryCounts() {
        return queryFactory
                .select(com.querydsl.core.types.Projections.constructor(SidebarDto.CategoryCount.class,
                        category.name, post.count()))
                .from(post)
                .join(post.category, category)
                .where(post.status.eq(PostStatus.PUBLIC))
                .groupBy(category.name)
                .fetch();
    }

    @Override
    public List<SidebarDto.SeriesCount> getSeriesCounts() {
        return queryFactory
                .select(com.querydsl.core.types.Projections.constructor(SidebarDto.SeriesCount.class,
                        series.name, post.count()))
                .from(post)
                .join(post.series, series)
                .where(post.status.eq(PostStatus.PUBLIC))
                .groupBy(series.name)
                .fetch();
    }

    @Override
    public List<String> getPublicTagNames() {
        return queryFactory
                .selectDistinct(tag.name)
                .from(postTag) // PostTag 테이블 기준
                .join(postTag.tag, tag)
                .join(postTag.post, post)
                .where(post.status.eq(PostStatus.PUBLIC))
                .fetch();
    }

    private void applySorting(JPAQuery<?> query, Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            query.orderBy(post.id.desc());
            return;
        }
        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "views": query.orderBy(new OrderSpecifier<>(direction, post.views)); break;
                case "createdAt": query.orderBy(new OrderSpecifier<>(direction, post.createdAt)); break;
                case "title": query.orderBy(new OrderSpecifier<>(direction, post.title)); break;
                default: query.orderBy(new OrderSpecifier<>(direction, post.id));
            }
        }
    }

    private BooleanExpression isPublic() {
        return post.status.eq(PostStatus.PUBLIC);
    }

    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword)
                ? post.title.containsIgnoreCase(keyword).or(post.content.containsIgnoreCase(keyword))
                : null;
    }

    private BooleanExpression categoryEq(String categoryName) {
        return StringUtils.hasText(categoryName)
                ? post.category.name.eq(categoryName)
                : null;
    }

    private BooleanExpression seriesEq(String seriesName) {
        return StringUtils.hasText(seriesName)
                ? post.series.name.eq(seriesName)
                : null;
    }

    private BooleanExpression tagEq(String tagName) {
        return StringUtils.hasText(tagName)
                ? tag.name.eq(tagName)
                : null;
    }
}