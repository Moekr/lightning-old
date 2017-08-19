package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.ArticleDao;
import com.moekr.blog.data.dao.CategoryDao;
import com.moekr.blog.data.dao.TagDao;
import com.moekr.blog.data.entity.Article;
import com.moekr.blog.data.entity.Category;
import com.moekr.blog.data.entity.Tag;
import com.moekr.blog.logic.vo.ArticleVo;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.ArticleDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleDao articleDao;
    private final CategoryDao categoryDao;
    private final TagDao tagDao;

    @Autowired
    public ArticleService(ArticleDao articleDao, CategoryDao categoryDao, TagDao tagDao) {
        this.articleDao = articleDao;
        this.categoryDao = categoryDao;
        this.tagDao = tagDao;
    }

    @Transactional
    public ArticleVo createArticle(ArticleDto articleDto){
        Article article = new Article();
        BeanUtils.copyProperties(articleDto, article, "category", "tags");
        article.setCreatedAt(LocalDateTime.now());
        article.setModifiedAt(article.getCreatedAt());
        article.setCategory(getCategory(articleDto.getCategory()));
        article.setTags(getTags(articleDto.getTags()));
        return new ArticleVo(articleDao.save(article));
    }

    public List<ArticleVo> getArticles(){
        return articleDao.findAll().stream().map(ArticleVo::new).collect(Collectors.toList());
    }

    public ArticleVo getArticle(int articleId){
        Article article = articleDao.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        return new ArticleVo(article);
    }

    @Transactional
    public ArticleVo updateArticle(int articleId, ArticleDto articleDto){
        Article article = articleDao.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        BeanUtils.copyProperties(articleDto, article, "category", "tags");
        article.setModifiedAt(LocalDateTime.now());
        article.setCategory(getCategory(articleDto.getCategory()));
        article.setTags(getTags(articleDto.getTags()));
        return new ArticleVo(articleDao.save(article));
    }

    @Transactional
    public ArticleVo deleteArticle(int articleId){
        Article article = articleDao.findById(articleId);
        ToolKit.assertNotNull(articleId, article);
        return new ArticleVo(articleDao.delete(article));
    }

    private Category getCategory(String categoryId){
        Category category = categoryDao.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        return category;
    }

    private Tag getTag(String tagId){
        Tag tag = tagDao.findById(tagId);
        ToolKit.assertNotNull(tagId, tag);
        return tag;
    }

    private List<Tag> getTags(List<String> tagIds){
        return tagIds.stream().map(this::getTag).collect(Collectors.toList());
    }
}
