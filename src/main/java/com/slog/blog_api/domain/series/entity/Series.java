package com.slog.blog_api.domain.series.entity;

import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "series")
public class Series extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "series")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Series(String name) {
        this.name = name;
    }
}
