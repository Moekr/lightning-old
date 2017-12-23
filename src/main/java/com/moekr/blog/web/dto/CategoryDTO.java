package com.moekr.blog.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CategoryDTO {
    @NotBlank
    private String name;
}
