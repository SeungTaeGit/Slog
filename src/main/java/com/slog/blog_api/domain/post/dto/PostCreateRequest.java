package com.slog.blog_api.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;
    private String categoryName;

    private List<String> tags;

    private String seriesName;

    public PostCreateRequest(String title, String content, String categoryName) {
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
    }
}
