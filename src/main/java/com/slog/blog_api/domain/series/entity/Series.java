package com.slog.blog_api.domain.series.entity;

import com.slog.blog_api.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "series")
public class Series extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "series_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    public Series(String name) {
        this.name = name;
    }
}
