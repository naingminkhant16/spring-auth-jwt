package com.moe.jwttest.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BlogRequest {

    @NotNull(message = "Blog title is required.")
    @Size(min = 1, max = 100, message = "Blog title should be between 1 and 100 chars.")
    private String title;

    @NotNull(message = "Blog content is required.")
    @Size(min = 1, max = 500, message = "Blog content should be between 1 and 500 chars.")
    private String content;
    
    private String image;

    @NotNull(message = "Author id is required.")
    @Positive
    private Long author_id;

}
