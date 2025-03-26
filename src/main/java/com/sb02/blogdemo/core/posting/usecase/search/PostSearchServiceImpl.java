package com.sb02.blogdemo.core.posting.usecase.search;

import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.port.PostRepositoryPort;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.posting.usecase.search.dto.SearchPostByKeywordCommand;
import com.sb02.blogdemo.core.posting.usecase.search.dto.SearchPostByTagCommand;
import com.sb02.blogdemo.core.user.entity.User;
import com.sb02.blogdemo.core.user.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostSearchServiceImpl implements PostSearchService {

    private final PostRepositoryPort postRepository;
    private final UserRepositoryPort userRepository;

    @Override
    public RetrievePostsResult searchByKeyword(SearchPostByKeywordCommand command) {
        String keyword = command.keyword().toLowerCase();

        List<Post> retrievedPosts = postRepository.findAll().stream()
                .filter(post -> postContainsKeyword(post, keyword))
                .toList();

        return getRetrievePostsResult(retrievedPosts, command.page(), command.size());
    }

    private boolean postContainsKeyword(Post post, String keyword) {
        return post.getTitle().toLowerCase().contains(keyword) || post.getContent().toLowerCase().contains(keyword);
    }

    private RetrievePostsResult getRetrievePostsResult(List<Post> retrievedPosts, long page, long size) {
        List<RetrievePostResult> retrievePostResults = retrievedPosts.stream()
                .skip(page * size)
                .limit(size)
                .map(this::convertToRetrievePostResult)
                .toList();

        int totalPages = (int) Math.ceil((double) retrievedPosts.size() / (double) size);

        return new RetrievePostsResult(
                retrievePostResults,
                page,
                size,
                totalPages
        );
    }

    private RetrievePostResult convertToRetrievePostResult(Post post) {
        String authorNickname = userRepository.findById(post.getAuthorId()).map(User::getNickname).orElse("");
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

    @Override
    public RetrievePostsResult searchByTag(SearchPostByTagCommand command) {
        String tag = command.tag().toLowerCase();

        List<Post> retrievedPosts = postRepository.findAll().stream()
                .filter(post -> postHasTag(post, tag))
                .toList();

        return getRetrievePostsResult(retrievedPosts, command.page(), command.size());
    }

    private boolean postHasTag(Post post, String tag) {
        return post.getTags().stream().anyMatch(t -> t.toLowerCase().equals(tag));
    }
}
