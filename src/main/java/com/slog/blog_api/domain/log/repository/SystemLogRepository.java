package com.slog.blog_api.domain.log.repository;

import com.slog.blog_api.domain.log.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    Page<SystemLog> findByLevel(String level, Pageable pageable);
}