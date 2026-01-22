package com.slog.blog_api.domain.series.repository;

import com.slog.blog_api.domain.series.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByName(String name);
}
