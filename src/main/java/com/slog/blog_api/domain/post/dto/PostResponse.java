package com.slog.blog_api.domain.post.dto;

import com.slog.blog_api.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final Long viewCount;
    private final LocalDateTime createdAt;
    private final String categoryName;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
        this.categoryName = (post.getCategory() != null) ? post.getCategory().getName() : null;
    }
}