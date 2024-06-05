package com.moe.jwttest.controller;

import com.moe.jwttest.payload.request.BlogRequest;
import com.moe.jwttest.entity.Blog;
import com.moe.jwttest.payload.response.ResourceResponse;
import com.moe.jwttest.payload.response.ApiResponse;
import com.moe.jwttest.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
@Tag(name = "Blog", description = "REST APIs Endpoints for Blog CRUD Operation")
public class BlogController {
    private final BlogService blogService;

    @GetMapping("")
    @Operation(summary = "Get All Blog", description = "Get blog by pagination,limit or search keyword.")
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
    @Operation(summary = "Create New Blog", description = "Create new blog.")
    public ResponseEntity<ApiResponse<BlogRequest>> createBlog(@Valid @RequestBody BlogRequest blogRequest) {
        BlogRequest createdBlog = blogService.save(blogRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.CREATED, "success", createdBlog)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Blog By ID", description = "Get blog by ID.")
    public ResponseEntity<ApiResponse<Blog>> getById(@PathVariable Long id) {
        Blog blog = blogService.findById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK, "success", blog)
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update existing Blog by ID", description = "Update existing blog by ID.")
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
    @Operation(summary = "Delete Blog by ID", description = "Delete existing blog by ID.")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        blogService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
