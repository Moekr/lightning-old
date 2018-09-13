package com.moekr.lightning.web.dto;

import com.moekr.lightning.util.ArticleType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class ArticleDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String summary;
    @NotBlank
    private String content;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    private String alias;
    @NotNull
    private ArticleType type;
    @NotNull
    private List<String> tags;
}
