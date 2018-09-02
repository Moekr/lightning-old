package com.moekr.lightning.data.dao;

import com.moekr.lightning.data.entity.Category;
import com.moekr.lightning.data.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDAO extends AbstractDAO<Category, String> {
    @Autowired
    public CategoryDAO(CategoryRepository repository) {
        super(repository);
    }
}
