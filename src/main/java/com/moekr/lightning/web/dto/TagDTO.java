package com.moekr.lightning.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class TagDTO {
    @NotBlank
    private String name;
}
