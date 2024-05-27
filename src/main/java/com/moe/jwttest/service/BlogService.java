package com.moe.jwttest.service;

import com.moe.jwttest.dto.BlogDto;
import com.moe.jwttest.entity.Blog;

import java.util.List;

public interface BlogService {
    List<Blog> findAll();

    Blog findById(Long id);

    BlogDto save(BlogDto blogDto);

    BlogDto updateById(BlogDto blogDto, Long id);

    void deleteById(Long id);
}
