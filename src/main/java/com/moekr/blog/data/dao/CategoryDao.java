package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Category;
import com.moekr.blog.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends AbstractDao<Category, String>{
    @Autowired
    public CategoryDao(CategoryRepository repository) {
        super(repository);
    }
}
