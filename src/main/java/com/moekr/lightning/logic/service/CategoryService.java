package com.moekr.lightning.logic.service;

import com.moekr.lightning.data.dao.CategoryDAO;
import com.moekr.lightning.data.entity.Category;
import com.moekr.lightning.logic.vo.CategoryVO;
import com.moekr.lightning.util.ToolKit;
import com.moekr.lightning.web.dto.CategoryDTO;
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

    private final CategoryDAO categoryDAO;

    @Autowired
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<CategoryVO> getCategories() {
        return categoryDAO.findAll().stream().map(CategoryVO::new).collect(Collectors.toList());
    }

    public CategoryVO getCategory(String categoryId) {
        Category category = categoryDAO.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        return new CategoryVO(category);
    }

    @Transactional
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
    public void deleteCategory(String categoryId) {
        Category category = categoryDAO.findById(categoryId);
        ToolKit.assertNotNull(categoryId, category);
        categoryDAO.delete(category);
    }
}
