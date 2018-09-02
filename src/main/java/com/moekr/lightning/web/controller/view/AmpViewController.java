package com.moekr.lightning.web.controller.view;

import com.moekr.lightning.logic.service.ArticleService;
import com.moekr.lightning.logic.service.CategoryService;
import com.moekr.lightning.logic.service.PropertyService;
import com.moekr.lightning.logic.vo.CategoryVO;
import com.moekr.lightning.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/amp")
public class AmpViewController {
    private final ArticleService articleService;
    private final PropertyService propertyService;
    private final CategoryService categoryService;

    @Autowired
    public AmpViewController(ArticleService articleService, PropertyService propertyService, CategoryService categoryService) {
        this.articleService = articleService;
        this.propertyService = propertyService;
        this.categoryService = categoryService;
    }

    @GetMapping({"/article/{articleId}", "/{alias}"})
    public String ampArticle(Map<String, Object> parameterMap, @PathVariable(required = false) Integer articleId, @PathVariable(required = false) String alias) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("article", alias == null ? articleService.viewArticle(articleId) : articleService.viewArticle(alias));
        parameterMap.put("parser", (Function<String, String>) ToolKit::parseAmpMarkdown);
        return "amp/article";
    }
}
