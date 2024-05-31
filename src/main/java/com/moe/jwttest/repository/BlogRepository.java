package com.moe.jwttest.repository;

import com.moe.jwttest.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query(
            value = "SELECT * FROM blogs WHERE title LIKE CONCAT('%',:search,'%') OR " +
                    "content LIKE CONCAT('%',:search,'%') ORDER BY id LIMIT :limit OFFSET :offset",
            nativeQuery = true
    )
    List<Blog> paginate(@Param("search") String search, @Param("limit") int limit, @Param("offset") int offset);
}
