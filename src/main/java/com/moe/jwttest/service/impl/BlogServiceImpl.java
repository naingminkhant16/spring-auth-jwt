package com.moe.jwttest.service.impl;

import com.moe.jwttest.payload.request.BlogRequest;
import com.moe.jwttest.entity.Blog;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.exception.ResourceNotFoundException;
import com.moe.jwttest.repository.BlogRepository;
import com.moe.jwttest.repository.UserRepository;
import com.moe.jwttest.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Blog> paginate(String search, int pageNo, int limit) {
        // if search key word is null, replace it with empty string ""
        if (search == null) search = "";

        //if provided page number is less than 1, 1 will be the default pageNO
        pageNo = Math.max(pageNo, 1);

        //if provided size limit is less than 1, 10 will be the default size limit
        limit = (limit < 1) ? 10 : limit;

        //calculate offset
        int offset = (pageNo - 1) * limit;

        return blogRepository.paginate(search, limit, offset);
    }

    @Override
    public BlogRequest save(BlogRequest blogRequest) {
        User author = userRepository
                .findById(blogRequest.getAuthor_id())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Author", "id", blogRequest.getAuthor_id().toString())
                );

        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setAuthor(author);
        Blog savedBlog = blogRepository.save(blog);

        return modelMapper.map(savedBlog, BlogRequest.class);
    }

    @Override
    public BlogRequest updateById(BlogRequest blogRequest, Long id) {
        Blog existingBlog = blogRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Blog", "id", id.toString())
                );

        existingBlog.setTitle(blogRequest.getTitle());
        existingBlog.setContent(blogRequest.getContent());

        Blog updatedBlog = blogRepository.save(existingBlog);

        return modelMapper.map(updatedBlog, BlogRequest.class);
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
