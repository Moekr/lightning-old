package com.moekr.lightning.web.controller.view;

import com.moekr.lightning.logic.service.*;
import com.moekr.lightning.logic.vo.ArticleVO;
import com.moekr.lightning.logic.vo.CategoryVO;
import com.moekr.lightning.logic.vo.TagVO;
import com.moekr.lightning.util.ToolKit;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ViewController {
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final PropertyService propertyService;
    private final SearchService searchService;

    @Autowired
    public ViewController(ArticleService articleService, CategoryService categoryService, TagService tagService, PropertyService propertyService, SearchService searchService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.propertyService = propertyService;
        this.searchService = searchService;
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

    @GetMapping({"/article/{articleId}", "/{alias}"})
    public String article(Map<String, Object> parameterMap, @PathVariable(required = false) Integer articleId, @PathVariable(required = false) String alias) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("article", alias == null ? articleService.viewArticle(articleId) : articleService.viewArticle(alias));
        parameterMap.put("parser", (Function<String, String>) ToolKit::parseMarkdown);
        return "article";
    }

    private static final ContentType MARKDOWN_CONTENT_TYPE = ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), Charset.forName("UTF-8"));

    @GetMapping({"/article/{articleId}.md", "/{alias}.md"})
    public void markdownArticle(HttpServletResponse response, @PathVariable(required = false) Integer articleId, @PathVariable(required = false) String alias) throws IOException {
        ArticleVO article = alias == null ? articleService.viewArticle(articleId) : articleService.viewArticle(alias);
        response.setContentType(MARKDOWN_CONTENT_TYPE.toString().toLowerCase());
        response.getWriter().append(article.getContent());
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

    @GetMapping("/search/{key}")
    public String search(Map<String, Object> parameterMap, @PathVariable String key) {
        parameterMap.put("properties", propertyService.getPropertiesAsMap());
        parameterMap.put("categories", ToolKit.sort(categoryService.getCategories().stream()
                .filter(CategoryVO::isVisible)
                .collect(Collectors.toList()), (a, b) -> b.getLevel() - a.getLevel()));
        parameterMap.put("articles", searchService.searchArticle(key).stream()
                .filter(ArticleVO::isVisible)
                .collect(Collectors.toList()));
        return "search";
    }
}
