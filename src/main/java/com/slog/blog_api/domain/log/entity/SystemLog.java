package com.slog.blog_api.domain.log.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SystemLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String level;

    private String method;
    private String url;

    @Column(length = 500)
    private String message;

    @Column(columnDefinition = "TEXT")
    private String stackTrace;

    private String clientIp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public SystemLog(String level, String method, String url, String message, String stackTrace, String clientIp) {
        this.level = level;
        this.method = method;
        this.url = url;
        this.message = message;
        this.stackTrace = stackTrace;
        this.clientIp = clientIp;
    }
}