package com.sb02.blogdemo.core.posting.usecase.search;

import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.posting.usecase.search.dto.SearchPostByKeywordCommand;
import com.sb02.blogdemo.core.posting.usecase.search.dto.SearchPostByTagCommand;

public interface PostSearchService {
    RetrievePostsResult searchByKeyword(SearchPostByKeywordCommand command);
    RetrievePostsResult searchByTag(SearchPostByTagCommand command);
}
