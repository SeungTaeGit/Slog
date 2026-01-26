package com.slog.blog_api.domain.post.dto;

import lombok.Data;

@Data
public class PostSearchCondition {
    private String keyword;
    private String categoryName;
    private String seriesName;
    private String tagName;
}