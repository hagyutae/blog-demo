package com.sb02.blogdemo.core.post.usecase.search;

import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.post.usecase.search.dto.SearchPostByKeywordCommand;
import com.sb02.blogdemo.core.post.usecase.search.dto.SearchPostByTagCommand;

public interface PostSearchService {
    RetrievePostsResult searchByKeyword(SearchPostByKeywordCommand command);
    RetrievePostsResult searchByTag(SearchPostByTagCommand command);
}
