package com.moekr.blog.web.controller.view;

import com.moekr.blog.logic.service.ArticleService;
import com.moekr.blog.logic.service.CategoryService;
import com.moekr.blog.logic.service.PropertyService;
import com.moekr.blog.logic.service.TagService;
import com.moekr.blog.logic.vo.TagVo;
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
    public String index(Map<String, Object> parameterMap){
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", categoryService.getCategories());
        parameterMap.put("articles", ToolKit.sort(articleService.getArticles(), (a, b) -> b.getId()-a.getId()));
        return "index";
    }

    @GetMapping("/article/{articleId}")
    public String article(Map<String, Object> parameterMap, @PathVariable int articleId){
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", categoryService.getCategories());
        parameterMap.put("article", articleService.viewArticle(articleId));
        parameterMap.put("parser", (Function<String, String>)ToolKit::parseMarkdown);
        return "article";
    }

    @GetMapping("/category/{categoryId}")
    public String category(Map<String, Object> parameterMap, @PathVariable String categoryId){
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", categoryService.getCategories());
        parameterMap.put("category", categoryService.getCategory(categoryId));
        parameterMap.put("articles", ToolKit.sort(articleService.getArticles().stream()
                .filter(articleVo -> categoryId.equals(articleVo.getCategory().getId()))
                .collect(Collectors.toList()), (a, b) -> b.getId()-a.getId()));
        return "category";
    }

    @GetMapping("/tag/{tagId}")
    public String tag(Map<String, Object> parameterMap, @PathVariable String tagId){
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", categoryService.getCategories());
        parameterMap.put("tag", tagService.getTag(tagId));
        parameterMap.put("articles", ToolKit.sort(articleService.getArticles().stream()
                .filter(articleVo -> articleVo.getTags().stream()
                        .map(TagVo::getId).anyMatch(tagId::equals))
                .collect(Collectors.toList()), (a, b) -> b.getId()-a.getId()));
        return "tag";
    }

    @GetMapping("/about")
    public String about(Map<String, Object> parameterMap){
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", categoryService.getCategories());
        parameterMap.put("parser", (Function<String, String>)ToolKit::parseMarkdown);
        return "about";
    }
}
