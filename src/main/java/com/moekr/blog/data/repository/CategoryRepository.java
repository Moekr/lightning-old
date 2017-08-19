package com.moekr.blog.data.repository;

import com.moekr.blog.data.entity.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, String> {
}
