package com.moekr.blog.web.controller.view;

import com.moekr.blog.logic.service.ArticleService;
import com.moekr.blog.logic.service.CategoryService;
import com.moekr.blog.logic.service.PropertyService;
import com.moekr.blog.logic.service.TagService;
import com.moekr.blog.logic.vo.ArticleVO;
import com.moekr.blog.logic.vo.CategoryVO;
import com.moekr.blog.logic.vo.TagVO;
import com.moekr.blog.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ViewController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final PropertyService propertyService;

    @Autowired
    public ViewController(ArticleService articleService, CategoryService categoryService, TagService tagService, PropertyService propertyService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.propertyService = propertyService;
    }

    @GetMapping("/")
    public String index(Map<String, Object> parameterMap) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("articles", ToolKit.sort(articleService.getArticles().stream()
                .filter(ArticleVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getId() - a.getId()));
        return "index";
    }

    @GetMapping("/article/{articleId}")
    public String article(Map<String, Object> parameterMap, @PathVariable int articleId) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("article", articleService.viewArticle(articleId));
        parameterMap.put("parser", (Function<String, String>) ToolKit::parseMarkdown);
        return "article";
    }

    @GetMapping("/category/{categoryId}")
    public String category(Map<String, Object> parameterMap, @PathVariable String categoryId) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("category", categoryService.getCategory(categoryId));
        parameterMap.put("articles", ToolKit.sort(articleService.getArticles().stream()
                .filter(ArticleVO::isVisible)
                .filter(articleVo -> categoryId.equals(articleVo.getCategory().getId()))
                .collect(Collectors.toList()), (a, b) -> b.getId() - a.getId()));
        return "category";
    }

    @GetMapping("/tag/{tagId}")
    public String tag(Map<String, Object> parameterMap, @PathVariable String tagId) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("tag", tagService.getTag(tagId));
        parameterMap.put("articles", ToolKit.sort(articleService.getArticles().stream()
                .filter(ArticleVO::isVisible)
                .filter(articleVo -> articleVo.getTags().stream()
                        .map(TagVO::getId).anyMatch(tagId::equals))
                .collect(Collectors.toList()), (a, b) -> b.getId() - a.getId()));
        return "tag";
    }

    @GetMapping("/archive")
    public String archive(Map<String, Object> parameterMap) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("months", ToolKit.sort(articleService.getArticles().stream()
                .filter(ArticleVO::isVisible)
                .collect(Collectors.groupingBy(article -> article.getCreatedAt().getYear() * article.getCreatedAt().getMonthValue()))
                .values().stream()
                .map(list -> ToolKit.sort(list, (a, b) -> b.getId() - a.getId()))
                .collect(Collectors.toList()), (a, b) -> b.get(0).getId() - a.get(0).getId()));
        return "archive";
    }

    @GetMapping("/{alias}")
    public String page(Map<String, Object> parameterMap, @PathVariable String alias) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("article", articleService.viewArticle(alias));
        parameterMap.put("parser", (Function<String, String>) ToolKit::parseMarkdown);
        return "article";
    }
}
