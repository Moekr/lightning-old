package com.moekr.lightning.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RedirectionDTO {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String id;
    @URL
    @NotBlank
    private String location;
}
