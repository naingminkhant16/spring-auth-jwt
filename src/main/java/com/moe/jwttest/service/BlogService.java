package com.moe.jwttest.service;

import com.moe.jwttest.payload.request.BlogRequest;
import com.moe.jwttest.entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> findAll();

    Blog findById(Long id);

    BlogRequest save(BlogRequest blogRequest);

    BlogRequest updateById(BlogRequest blogRequest, Long id);

    void deleteById(Long id);

    List<Blog> paginate(String search, int pageNo, int limit);
}
