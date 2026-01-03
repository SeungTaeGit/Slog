package com.slog.blog_api.domain.post.entity;

import com.slog.blog_api.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String thumbnailUrl;

    private Long viewCount;

    // 공개 상태 (PUBLIC, PRIVATE, DRAFT) -> Enum으로 관리하면 더 좋아 (일단 String)
    @Column(nullable = false)
    private String status;

    @Builder
    public Post(String title, String content, String thumbnailUrl, String status) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
        this.viewCount = 0L;
    }

    public void update(String title, String content, String thumbnailUrl, String status) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.status = status;
    }
}
