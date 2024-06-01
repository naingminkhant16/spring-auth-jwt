package com.moe.jwttest.controller;

import com.moe.jwttest.payload.request.BlogRequest;
import com.moe.jwttest.entity.Blog;
import com.moe.jwttest.payload.response.ResourceResponse;
import com.moe.jwttest.payload.response.ApiResponse;
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
    public ResponseEntity<ResourceResponse<List<Blog>>> getAllBlogs(
            @RequestParam(name = "page", defaultValue = "1", required = false) int pageNo,
            @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
            @RequestParam(name = "search", defaultValue = "", required = false) String search
    ) {
        List<Blog> blogs = blogService.paginate(search, pageNo, limit);

        return ResponseEntity.ok(
                new ResourceResponse<>(pageNo, limit, HttpStatus.OK, "success", blogs)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<ApiResponse<BlogRequest>> createBlog(@Valid @RequestBody BlogRequest blogRequest) {
        BlogRequest createdBlog = blogService.save(blogRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.CREATED, "success", createdBlog)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Blog>> getById(@PathVariable Long id) {
        Blog blog = blogService.findById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK, "success", blog)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BlogRequest>> updateBlogById(
            @Valid @RequestBody BlogRequest blogRequest,
            @PathVariable Long id
    ) {
        BlogRequest updatedBlog = blogService.updateById(blogRequest, id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK, "successfully updated", updatedBlog)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        blogService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
