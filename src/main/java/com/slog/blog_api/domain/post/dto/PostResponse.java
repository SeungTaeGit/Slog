package com.slog.blog_api.domain.post.dto;

import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.domain.post.entity.PostStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final Long views;
    private final LocalDateTime createdAt;
    private final String categoryName;
    private final List<String> tags;
    private final String seriesName;
    private final PostStatus status;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.views = post.getViews();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.categoryName = (post.getCategory() != null) ? post.getCategory().getName() : null;
        this.tags = post.getPostTags().stream()
                .map(postTag -> postTag.getTag().getName())
                .collect(Collectors.toList());
        this.seriesName = post.getSeries() != null ? post.getSeries().getName() : null;
    }
}