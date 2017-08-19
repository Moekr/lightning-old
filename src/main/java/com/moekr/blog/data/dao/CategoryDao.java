package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Category;
import com.moekr.blog.data.repository.CategoryRepository;
import com.moekr.blog.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDao {
    private final CategoryRepository repository;

    @Autowired
    public CategoryDao(CategoryRepository repository) {
        this.repository = repository;
    }

    public Category save(Category category){
        return repository.save(category);
    }

    public List<Category> findAll(){
        return ToolKit.iterableToList(repository.findAll());
    }

    public Category findById(String categoryId){
        return repository.findOne(categoryId);
    }

    public void delete(Category category){
        repository.delete(category);
    }
}
