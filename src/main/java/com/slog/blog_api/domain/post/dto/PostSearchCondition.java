package com.slog.blog_api.domain.post.dto;

import com.slog.blog_api.domain.post.entity.PostStatus;
import lombok.Data;

@Data
public class PostSearchCondition {
    private String keyword;
    private String categoryName;
    private String seriesName;
    private String tagName;
    private PostStatus status;
}