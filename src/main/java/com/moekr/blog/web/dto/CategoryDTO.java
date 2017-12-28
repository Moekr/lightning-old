package com.moekr.blog.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDTO {
    @NotBlank
    private String name;
    @Range(min = 0, max = 100)
    private int level;
    @NotNull
    private boolean visible;
}
