package com.moekr.blog.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PropertyDto {
    @NotNull
    private String value;
}
