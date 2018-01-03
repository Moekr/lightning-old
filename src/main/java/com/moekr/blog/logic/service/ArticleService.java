package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.ArticleDAO;
import com.moekr.blog.data.dao.CategoryDAO;
import com.moekr.blog.data.dao.TagDAO;
import com.moekr.blog.data.entity.Article;
import com.moekr.blog.data.entity.Category;
import com.moekr.blog.data.entity.Tag;
import com.moekr.blog.logic.vo.ArticleVO;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.ArticleDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "service-cache")
public class ArticleService {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    private final ArticleDAO articleDAO;
    private final CategoryDAO categoryDAO;
    private final TagDAO tagDAO;

    @Autowired
    public ArticleService(ArticleDAO articleDAO, CategoryDAO categoryDAO, TagDAO tagDAO) {
        this.articleDAO = articleDAO;
        this.categoryDAO = categoryDAO;
        this.tagDAO = tagDAO;
    }

    @Transactional
    @Caching(put = @CachePut(key = "'article-'+#result.id"), evict = @CacheEvict(key = "'article-'+'articleList'"))
    public ArticleVO createArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article, "alias", "category", "tags");
        if (!articleDTO.getAlias().isEmpty()) {
            ToolKit.assertPattern(articleDTO.getAlias(), PATTERN);
            article.setAlias(articleDTO.getAlias());
        }
        article.setCreatedAt(LocalDateTime.now());
        article.setModifiedAt(article.getCreatedAt());
        article.setCategory(getCategory(articleDTO.getCategory()));
        article.setTags(getTags(articleDTO.getTags()));
        return new ArticleVO(articleDAO.save(article));
    }

    @Cacheable(key = "'article-'+'articleList'")
    public List<ArticleVO> getArticles() {
        return articleDAO.findAll().stream().map(ArticleVO::new).collect(Collectors.toList());
    }

    @Cacheable(key = "'article-'+#articleId")
    public ArticleVO getArticle(int articleId) {
        Article article = articleDAO.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        return new ArticleVO(article);
    }

    @Transactional
    @Caching(put = @CachePut(key = "'article-'+#articleId"), evict = @CacheEvict(key = "'article-'+'articleList'"))
    public ArticleVO viewArticle(int articleId) {
        Article article = articleDAO.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        ToolKit.assertVisible(article);
        article.setViews(article.getViews() + 1);
        return new ArticleVO(articleDAO.save(article));
    }

    @Transactional
    @Caching(put = @CachePut(key = "'article-'+#result.id"), evict = @CacheEvict(key = "'article-'+'articleList'"))
    public ArticleVO viewArticle(String alias) {
        Article article = articleDAO.findByAlias(alias);
        ToolKit.assertNotNull(alias, article);
        article.setViews(article.getViews() + 1);
        return new ArticleVO(articleDAO.save(article));
    }

    @Transactional
    @Caching(put = @CachePut(key = "'article-'+#articleId"), evict = @CacheEvict(key = "'article-'+'articleList'"))
    public ArticleVO updateArticle(int articleId, ArticleDTO articleDTO) {
        Article article = articleDAO.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        BeanUtils.copyProperties(articleDTO, article, "alias", "category", "tags");
        if (!articleDTO.getAlias().isEmpty()) {
            ToolKit.assertPattern(articleDTO.getAlias(), PATTERN);
            article.setAlias(articleDTO.getAlias());
        } else {
            article.setAlias(null);
        }
        article.setModifiedAt(LocalDateTime.now());
        article.setCategory(getCategory(articleDTO.getCategory()));
        article.setTags(getTags(articleDTO.getTags()));
        return new ArticleVO(articleDAO.save(article));
    }

    @Transactional
    @Caching(evict = {@CacheEvict(key = "'article-'+#articleId"), @CacheEvict(key = "'article-'+'articleList'")})
    public void deleteArticle(int articleId) {
        Article article = articleDAO.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        articleDAO.delete(article);
    }

    private Category getCategory(String categoryId) {
        Category category = categoryDAO.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        return category;
    }

    private Tag getTag(String tagId) {
        Tag tag = tagDAO.findById(tagId);
        ToolKit.assertNotNull(tagId, tag);
        return tag;
    }

    private List<Tag> getTags(List<String> tagIds) {
        return tagIds.stream().map(this::getTag).collect(Collectors.toList());
    }
}
