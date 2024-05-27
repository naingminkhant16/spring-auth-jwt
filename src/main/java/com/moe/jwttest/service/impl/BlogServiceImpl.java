package com.moe.jwttest.service.impl;

import com.moe.jwttest.dto.BlogDto;
import com.moe.jwttest.entity.Blog;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.exception.ResourceNotFoundException;
import com.moe.jwttest.repository.BlogRepository;
import com.moe.jwttest.repository.UserRepository;
import com.moe.jwttest.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Blog findById(Long id) {

        return blogRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id.toString()));
    }

    @Override
    public BlogDto save(BlogDto blogDto) {
        User author = userRepository
                .findById(blogDto.getAuthor_id())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Author", "id", blogDto.getAuthor_id().toString())
                );

        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setContent(blogDto.getContent());
        blog.setAuthor(author);
        Blog savedBlog = blogRepository.save(blog);

        return modelMapper.map(savedBlog, BlogDto.class);
    }

    @Override
    public BlogDto updateById(BlogDto blogDto, Long id) {
        Blog existingBlog = blogRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Blog", "id", id.toString())
                );

        existingBlog.setTitle(blogDto.getTitle());
        existingBlog.setContent(blogDto.getContent());

        Blog updatedBlog = blogRepository.save(existingBlog);

        return modelMapper.map(updatedBlog, BlogDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Blog existingBlog = blogRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Blog", "id", id.toString())
                );

        blogRepository.delete(existingBlog);
    }
}
