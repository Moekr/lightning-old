package com.moekr.blog.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CategoryDto {
    @NotBlank
    private String name;
}
