package com.slog.blog_api.domain.admin.service;

import com.slog.blog_api.domain.admin.dto.AdminDashboardDto;
import com.slog.blog_api.domain.category.entity.Category;
import com.slog.blog_api.domain.category.repository.CategoryRepository;
import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.domain.post.entity.PostStatus;
import com.slog.blog_api.domain.post.repository.PostRepository;
import com.slog.blog_api.domain.series.entity.Series;
import com.slog.blog_api.domain.series.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final SeriesRepository seriesRepository;

    public AdminDashboardDto getDashboardStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();

        return AdminDashboardDto.builder()
                .totalPosts(postRepository.count())
                .totalViews(postRepository.getTotalViews()) // 이 메서드는 아래에서 추가할게요!
                .todayPosts(postRepository.countByCreatedAtAfter(startOfDay))
                .totalCategories(postRepository.countDistinctCategories())
                .totalSeries(postRepository.countDistinctSeries())
                .build();
    }

    @Transactional
    public void updateCategory(Long id, String newName) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        category.setName(newName);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        if (!category.getPosts().isEmpty()) {
            throw new IllegalStateException("게시글이 포함된 카테고리는 삭제할 수 없습니다. 글을 먼저 이동시켜주세요.");
        }

        categoryRepository.delete(category);
    }

    @Transactional
    public void updateSeries(Long id, String newName) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시리즈입니다."));
        series.setName(newName);
    }

    @Transactional
    public void deleteSeries(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시리즈입니다."));

        if (!series.getPosts().isEmpty()) {
            throw new IllegalStateException("게시글이 포함된 시리즈는 삭제할 수 없습니다.");
        }
        seriesRepository.delete(series);
    }

    @Transactional
    public void changePostStatus(Long id, PostStatus status) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));
        post.setStatus(status);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        if (post.getStatus() == PostStatus.DELETED) {
            throw new IllegalStateException("이미 삭제된 게시글입니다.");
        }

        post.setStatus(PostStatus.DELETED);
    }

    @Transactional
    public void restorePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        post.setStatus(PostStatus.PUBLIC);
    }

    @Transactional
    public void hardDeletePost(Long id) {
        postRepository.deleteById(id);
    }
}