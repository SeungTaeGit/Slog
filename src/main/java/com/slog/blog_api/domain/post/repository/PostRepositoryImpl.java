package com.slog.blog_api.domain.post.repository;

import com.slog.blog_api.domain.post.dto.SidebarDto;
import com.slog.blog_api.domain.post.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import com.slog.blog_api.domain.post.entity.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.slog.blog_api.domain.post.entity.QPost.post;
import static com.slog.blog_api.domain.category.entity.QCategory.category;
import static com.slog.blog_api.domain.series.entity.QSeries.series;
import static com.slog.blog_api.domain.tag.entity.QTag.tag;
import static com.slog.blog_api.domain.tag.entity.QPostTag.postTag;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> search(String keyword, String categoryName, String tagName, String seriesName, Pageable pageable) { // 파라미터 변경됨

        List<Post> content = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.category, category).fetchJoin()
                .leftJoin(post.series, series).fetchJoin()
                .leftJoin(post.postTags, postTag)
                .leftJoin(postTag.tag, tag)
                .where(
                        containsKeyword(keyword),
                        eqCategory(categoryName),
                        eqSeries(seriesName),
                        eqTag(tagName)
                )
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(post.countDistinct())
                .from(post)
                .leftJoin(post.postTags, postTag)
                .leftJoin(postTag.tag, tag)
                .leftJoin(post.series, series)
                .where(
                        containsKeyword(keyword),
                        eqCategory(categoryName),
                        eqSeries(seriesName),
                        eqTag(tagName)
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

    private BooleanExpression eqSeries(String seriesName) {
        if (seriesName == null || seriesName.isEmpty()) {
            return null;
        }
        return series.name.eq(seriesName);
    }

    private BooleanExpression eqTag(String tagName) {
        if (tagName == null || tagName.isEmpty()) {
            return null;
        }
        return tag.name.eq(tagName);
    }

    @Override
    public List<SidebarDto.CategoryCount> getCategoryCounts() {
        return jpaQueryFactory
                .select(Projections.constructor(SidebarDto.CategoryCount.class,
                        category.name,
                        post.count()))
                .from(post)
                .join(post.category, category)
                .where(post.status.eq(PostStatus.PUBLIC))
                .groupBy(category.name)
                .fetch();
    }

    @Override
    public List<SidebarDto.SeriesCount> getSeriesCounts() {
        return jpaQueryFactory
                .select(Projections.constructor(SidebarDto.SeriesCount.class,
                        series.name,
                        post.count()))
                .from(post)
                .join(post.series, series)
                .where(post.status.eq(PostStatus.PUBLIC))
                .groupBy(series.name)
                .fetch();
    }

    @Override
    public List<String> getPublicTagNames() {
        return jpaQueryFactory
                .selectDistinct(tag.name)
                .from(postTag)
                .join(postTag.tag, tag)
                .join(postTag.post, post)
                .where(post.status.eq(PostStatus.PUBLIC))
                .fetch();
    }
}