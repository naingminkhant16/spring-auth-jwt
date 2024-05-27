package com.moe.jwttest.controller;

import com.moe.jwttest.dto.BlogDto;
import com.moe.jwttest.entity.Blog;
import com.moe.jwttest.response.ApiResponse;
import com.moe.jwttest.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Blog>>> getAllBlogs() {
        List<Blog> blogs = blogService.findAll();

        return ResponseEntity.ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "success", blogs)
                );
    }
    
    @PostMapping("")
    public ResponseEntity<ApiResponse<BlogDto>> createBlog(@Valid @RequestBody BlogDto blogDto) {
        BlogDto createdBlog = blogService.save(blogDto);

        return ResponseEntity.ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "success", createdBlog)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Blog>> getById(@PathVariable Long id) {
        Blog blog = blogService.findById(id);

        return ResponseEntity.ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "success", blog)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogDto>> updateBlogById(
            @Valid @RequestBody BlogDto blogDto,
            @PathVariable Long id
    ) {
        BlogDto updatedBlog = blogService.updateById(blogDto, id);

        return ResponseEntity.ok()
                .body(
                        new ApiResponse<>(HttpStatus.OK, "successfully updated", updatedBlog)
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        blogService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
