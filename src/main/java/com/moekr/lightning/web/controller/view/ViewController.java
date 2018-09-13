package com.moekr.lightning.web.controller.view;

import com.moekr.lightning.logic.service.ArticleService;
import com.moekr.lightning.logic.vo.ArticleVO;
import com.moekr.lightning.util.PageNumberEditor;
import com.moekr.lightning.util.ToolKit;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ViewController {
    private static final int PAGE_SIZE = 10;

    private final ArticleService articleService;

    @Autowired
    public ViewController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getObjectName().equals("p")) {
            binder.registerCustomEditor(int.class, new PageNumberEditor());
        }
    }

    @GetMapping({"/", "/index.html"})
    public String index(Map<String, Object> parameterMap, @RequestParam(name = "p", defaultValue = "1") int page) {
        parameterMap.put("articles", articleService.viewArticles(page, PAGE_SIZE));
        return "index";
    }

    @GetMapping("/article/{articleId:\\d+}")
    public String article(@PathVariable int articleId) {
        ArticleVO article = articleService.viewArticle(articleId);
        return "redirect:/article/" + article.getAlias() + ".html";
    }

    @GetMapping("/article/{alias}.html")
    public String htmlArticle(Map<String, Object> parameterMap, @PathVariable String alias) {
        parameterMap.put("article", articleService.viewArticle(alias));
        parameterMap.put("parser", (Function<String, String>) ToolKit::parseMarkdown);
        return "article";
    }

    private static final ContentType MARKDOWN_CONTENT_TYPE =
            ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), Charset.forName("UTF-8"));

    @GetMapping("/article/{alias}.md")
    public void markdownArticle(HttpServletResponse response, @PathVariable String alias) throws IOException {
        ArticleVO article = articleService.viewArticle(alias);
        response.setContentType(MARKDOWN_CONTENT_TYPE.toString().toLowerCase());
        response.getWriter().append(article.getContent());
    }

    @GetMapping("/page/{alias}.html")
    public String page(Map<String, Object> parameterMap, @PathVariable String alias) {
        parameterMap.put("article", articleService.viewPage(alias));
        parameterMap.put("parser", (Function<String, String>) ToolKit::parseMarkdown);
        return "article";
    }

    @GetMapping("/archive.html")
    public String archive(Map<String, Object> parameterMap) {
        parameterMap.put("months", ToolKit.sort(new ArrayList<>(articleService.viewArticles(0, Integer.MAX_VALUE).stream()
                .collect(Collectors.groupingBy(article -> article.getCreatedAt().getYear() * article.getCreatedAt().getMonthValue()))
                .values()), (a, b) -> b.get(0).getId() - a.get(0).getId()));
        return "archive";
    }

    @GetMapping(value = "/search.html")
    public String search(Map<String, Object> parameterMap, @RequestParam(value = "q", required = false) String query,
                         @RequestParam(name = "p", defaultValue = "1") int page) {
        if (query == null) {
            query = "";
        }
        parameterMap.put("articles", articleService.searchArticles(page, PAGE_SIZE, query));
        return "search";
    }
}
