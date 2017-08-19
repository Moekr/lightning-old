package com.moekr.blog.logic.service;

import com.moekr.blog.data.dao.CategoryDao;
import com.moekr.blog.data.entity.Category;
import com.moekr.blog.logic.vo.CategoryVo;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.CategoryDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryVo> getCategories(){
        return categoryDao.findAll().stream().map(CategoryVo::new).collect(Collectors.toList());
    }

    public CategoryVo getCategory(String categoryId){
        Category category = categoryDao.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        return new CategoryVo(category);
    }

    @Transactional
    public CategoryVo updateCategory(String categoryId, CategoryDto categoryDto){
        Category category = categoryDao.findById(categoryId);
        if(category == null){
            ToolKit.assertPattern(categoryId, PATTERN);
            category = new Category();
            category.setId(categoryId);
        }
        BeanUtils.copyProperties(categoryDto, category);
        return new CategoryVo(categoryDao.save(category));
    }

    @Transactional
    public CategoryVo deleteCategory(String categoryId){
        Category category = categoryDao.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        categoryDao.delete(category);
        return new CategoryVo(category);
    }
}
