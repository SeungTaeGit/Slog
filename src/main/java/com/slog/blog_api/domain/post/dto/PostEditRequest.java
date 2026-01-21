package com.slog.blog_api.domain.post.dto;

import com.slog.blog_api.domain.post.entity.PostStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostEditRequest {
    private String title;
    private String content;
    private String categoryName;
    private PostStatus status; // 수정할 때는 상태도 변경 가능
}
