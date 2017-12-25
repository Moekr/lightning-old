package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.CategoryDAO;
import com.moekr.blog.data.entity.Category;
import com.moekr.blog.logic.vo.CategoryVO;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.CategoryDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "service-cache")
public class CategoryService {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    private final CategoryDAO categoryDAO;

    @Autowired
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Cacheable(key = "'category-'+'categoryList'")
    public List<CategoryVO> getCategories() {
        return categoryDAO.findAll().stream().map(CategoryVO::new).collect(Collectors.toList());
    }

    @Cacheable(key = "'category-'+#categoryId")
    public CategoryVO getCategory(String categoryId) {
        Category category = categoryDAO.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        return new CategoryVO(category);
    }

    @Transactional
    @Caching(put = @CachePut(key = "'category-'+#categoryId"), evict = @CacheEvict(key = "'category-'+'categoryList'"))
    public CategoryVO updateCategory(String categoryId, CategoryDTO categoryDTO) {
        Category category = categoryDAO.findById(categoryId);
        if (category == null) {
            ToolKit.assertPattern(categoryId, PATTERN);
            category = new Category();
            category.setId(categoryId);
        }
        BeanUtils.copyProperties(categoryDTO, category);
        return new CategoryVO(categoryDAO.save(category));
    }

    @Transactional
    @Caching(evict = {@CacheEvict(key = "'category-'+#categoryId"), @CacheEvict(key = "'category-'+'categoryList'")})
    public void deleteCategory(String categoryId) {
        Category category = categoryDAO.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        categoryDAO.delete(category);
    }
}
