package com.moekr.blog.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class TagDto {
    @NotBlank
    private String name;
}
