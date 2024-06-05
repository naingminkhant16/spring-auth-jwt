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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @Value("${image-storage-path}")
    private String imageStoragePath;

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
                //store image in images folder under src/main/resources/static/
                byte[] decodedBytes = Base64.getDecoder().decode(blogRequest.getImage());  // decode the image
                String imageName = new Date().getTime() + ".png";// generate image name
                Path toStorePath = Paths.get(imageStoragePath).resolve(imageName);
                Files.write(toStorePath, decodedBytes); // write file in static/images

                //generate image url
                String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/images/")
                        .path(imageName)
                        .toUriString();

                blog.setImage(imageUrl);//save image url in db
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
        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", "id", id.toString()));

        try {
            if (blogRequest.getImage() != null) {
                Path path = Paths.get(imageStoragePath);
                // delete old image file
                String oldImageName = Paths.get(
                                new URI(existingBlog.getImage()).getPath()
                        )
                        .getFileName()
                        .toString();
                Path toDeletePath = path.resolve(oldImageName);
                Files.deleteIfExists(toDeletePath);

                // Store new image file
                byte[] decodedBytes = Base64.getDecoder().decode(blogRequest.getImage());
                String imageName = new Date().getTime() + ".png";
                Path toStorePath = path.resolve(imageName);
                Files.write(toStorePath, decodedBytes);

                // generate image url to store in db
                String newImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/images/")
                        .path(imageName)
                        .toUriString();
                //save to entity
                existingBlog.setImage(newImageUrl);
            }

            existingBlog.setTitle(blogRequest.getTitle());
            existingBlog.setContent(blogRequest.getContent());

            Blog updatedBlog = blogRepository.save(existingBlog);

            return modelMapper.map(updatedBlog, BlogRequest.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        Blog existingBlog = blogRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Blog", "id", id.toString())
                );

        try {
            // also delete the associated image
            String imageUrl = existingBlog.getImage();//Eg. http://localhost:8080/images/image1.png
            if (imageUrl != null) {
                // extract the file name from the URL
                String fileName = Paths.get(
                                new URI(imageUrl) // will return 'images/image1.png'
                                        .getPath() // convert into Path obj
                        )
                        .getFileName() // will return 'image1.png'
                        .toString();

                Path imagePath = Paths.get(imageStoragePath).resolve(fileName);
                Files.deleteIfExists(imagePath); //delete image
            }

            blogRepository.delete(existingBlog);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete the image file", e);
        }
    }
}
