package com.slog.blog_api.domain.post.entity;

import com.slog.blog_api.domain.category.entity.Category;
import com.slog.blog_api.domain.series.entity.Series;
import com.slog.blog_api.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.slog.blog_api.domain.tag.entity.PostTag;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @Builder
    public Post(String title, String content, String thumbnailUrl, Category category, PostStatus status, Series series) {
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
        this.status = status;
        this.series = series;
    }

    public void update(String title, String content, Category category, PostStatus status, Series series) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.status = status;
        this.series = series;
    }

    public void addPostTag(PostTag postTag) {
        this.postTags.add(postTag);
    }
}
