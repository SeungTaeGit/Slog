package com.slog.blog_api.domain.post.dto;

import com.slog.blog_api.domain.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "본문은 필수입니다.")
    private String content;

    private String status;

    private String thumbnailUrl;

    private String categoryName;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .build();
    }
}