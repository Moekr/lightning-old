package com.moekr.blog.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class RedirectionDto {
    @NotBlank
    private String id;
    @NotBlank
    private String location;
}
