package com.moekr.lightning.data.repository;

import com.moekr.lightning.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
