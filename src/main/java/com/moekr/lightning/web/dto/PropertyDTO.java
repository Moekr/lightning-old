package com.moekr.lightning.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PropertyDTO {
    @NotNull
    private String value;
}
