package com.sb02.blogdemo.core.post.usecase.search;

import com.sb02.blogdemo.core.post.entity.Post;
import com.sb02.blogdemo.core.post.port.PostRepositoryPort;
import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.post.usecase.search.dto.SearchPostByKeywordCommand;
import com.sb02.blogdemo.core.post.usecase.search.dto.SearchPostByTagCommand;
import com.sb02.blogdemo.core.user.entity.User;
import com.sb02.blogdemo.core.user.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostRepositoryPort postRepository;
    private final UserRepositoryPort userRepository;

    @Override
    public RetrievePostsResult searchByKeyword(SearchPostByKeywordCommand command) {
        String keyword = command.keyword().toLowerCase();

        return searchPosts(
                post -> post.getTitle().toLowerCase().contains(keyword) ||
                        post.getContent().toLowerCase().contains(keyword),
                command.page(),
                command.size()
        );
    }

    @Override
    public RetrievePostsResult searchByTag(SearchPostByTagCommand command) {
        String tag = command.tag().toLowerCase();

        return searchPosts(
                post -> post.getTags().stream()
                        .map(String::toLowerCase)
                        .anyMatch(t -> t.equals(tag)),
                command.page(),
                command.size()
        );
    }

    private RetrievePostsResult searchPosts(Predicate<Post> filter, long page, long size) {
        return postRepository.findAll().stream()
                .filter(filter)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        posts -> paginateAndConvert(posts, page, size)
                ));
    }

    private RetrievePostsResult paginateAndConvert(List<Post> posts, long page, long size) {
        List<RetrievePostResult> pageResults = posts.stream()
                .skip(page * size)
                .limit(size)
                .map(this::toRetrievePostResult)
                .toList();

        long totalPages = calculateTotalPages(posts.size(), size);

        return new RetrievePostsResult(pageResults, page, size, totalPages);
    }

    private long calculateTotalPages(int itemCount, long pageSize) {
        return (long) Math.ceil((double) itemCount / pageSize);
    }

    private RetrievePostResult toRetrievePostResult(Post post) {
        return userRepository.findById(post.getAuthorId())
                .map(User::getNickname)
                .map(nickname -> createPostResult(post, nickname))
                .orElseGet(() -> createPostResult(post, ""));
    }

    private RetrievePostResult createPostResult(Post post, String authorNickname) {
        return new RetrievePostResult(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getTags(),
                post.getAuthorId(),
                authorNickname,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}