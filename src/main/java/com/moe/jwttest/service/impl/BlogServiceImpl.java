package com.moe.jwttest.service.impl;

import com.moe.jwttest.payload.request.BlogRequest;
import com.moe.jwttest.entity.Blog;
import com.moe.jwttest.entity.User;
import com.moe.jwttest.exception.ResourceNotFoundException;
import com.moe.jwttest.repository.BlogRepository;
import com.moe.jwttest.repository.UserRepository;
import com.moe.jwttest.service.BlogService;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final String imagePath = "resources/images";
    private final ServletContext context;

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

        try {
            Blog blog = new Blog();

            if (blogRequest.getImage() != null) {
                // create directories if they do not exist
                Path imagePath = Paths.get("src/main/resources/static/images");
                if (!Files.exists(imagePath)) {
                    Files.createDirectories(imagePath);
                }

                //store image in images folder under src/main/resources/static/
                byte[] decodedBytes = Base64.getDecoder().decode(blogRequest.getImage());  //decode the image
                Path path = imagePath.resolve(new Date().getTime() + ".png");
                Files.write(path, decodedBytes);//file write
                blog.setImage(path.toString());
            }

            blog.setTitle(blogRequest.getTitle());
            blog.setContent(blogRequest.getContent());
            blog.setAuthor(author);
            //save to db
            Blog savedBlog = blogRepository.save(blog);

            return modelMapper.map(savedBlog, BlogRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
