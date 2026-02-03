package com.slog.blog_api.domain.post.service;

import com.slog.blog_api.domain.category.entity.Category;
import com.slog.blog_api.domain.category.repository.CategoryRepository;
import com.slog.blog_api.domain.post.dto.PostCreateRequest;
import com.slog.blog_api.domain.post.dto.PostEditRequest;
import com.slog.blog_api.domain.post.dto.PostResponse;
import com.slog.blog_api.domain.post.dto.PostSearchCondition;
import com.slog.blog_api.domain.post.entity.Post;
import com.slog.blog_api.domain.post.entity.PostStatus;
import com.slog.blog_api.domain.post.repository.PostRepository;
import com.slog.blog_api.domain.series.entity.Series;
import com.slog.blog_api.domain.series.repository.SeriesRepository;
import com.slog.blog_api.domain.tag.entity.PostTag;
import com.slog.blog_api.domain.tag.entity.Tag;
import com.slog.blog_api.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final SeriesRepository seriesRepository;

    @Transactional
    public Long writePost(PostCreateRequest request) {
        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(request.getCategoryName())
                        .build()));

        Series series = getSeries(request.getSeriesName());

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .thumbnailUrl("https://image.com/default.png")
                .category(category)
                .series(series)
//                .status(PostStatus.PUBLIC)
                .build();

        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder()
                                .name(tagName)
                                .build()));

                PostTag postTag = PostTag.builder()
                        .post(post)
                        .tag(tag)
                        .build();

                post.addPostTag(postTag);
            }
        }

        postRepository.save(post);
        return post.getId();
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        return new PostResponse(post);
    }

    public Page<PostResponse> getPostList(PostSearchCondition condition, Pageable pageable) {
        return postRepository.search(condition, pageable);
    }

    @Transactional
    public void editPost(Long postId, PostEditRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Category category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(request.getCategoryName())
                        .build()));

        Series series = getSeries(request.getSeriesName());

        String title = request.getTitle() != null ? request.getTitle() : post.getTitle();
        String content = request.getContent() != null ? request.getContent() : post.getContent();
        PostStatus status = request.getStatus() != null ? request.getStatus() : post.getStatus();

        post.update(title, content, category, status, series);

        if (request.getTags() != null) {
            post.getPostTags().clear();

            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder()
                                .name(tagName)
                                .build()));

                PostTag postTag = PostTag.builder()
                        .post(post)
                        .tag(tag)
                        .build();

                post.addPostTag(postTag);
            }
        }
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        postRepository.delete(post);
    }

    private Series getSeries(String seriesName) {
        if (seriesName == null || seriesName.isEmpty()) {
            return null;
        }
        return seriesRepository.findByName(seriesName)
                .orElseGet(() -> seriesRepository.save(Series.builder()
                        .name(seriesName)
                        .build()));
    }

    @Transactional
    public void incrementViews(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        post.setViews(post.getViews() + 1);
    }
}