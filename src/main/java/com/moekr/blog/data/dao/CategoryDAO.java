package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Category;
import com.moekr.blog.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAO extends AbstractDAO<Category, String> {
    @Autowired
    public CategoryDAO(CategoryRepository repository) {
        super(repository);
    }
}
