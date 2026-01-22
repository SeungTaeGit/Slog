package com.slog.blog_api.domain.post.dto;

import com.slog.blog_api.domain.post.entity.PostStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostEditRequest {

    private String title;

    private String content;

    private String categoryName;

    private PostStatus status;

    private List<String> tags;

    private String seriesName;
}
