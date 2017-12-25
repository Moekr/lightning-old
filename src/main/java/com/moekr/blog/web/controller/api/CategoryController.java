package com.moekr.blog.web.controller.api;

import com.moekr.blog.logic.service.CategoryService;
import com.moekr.blog.util.ToolKit;
import com.moekr.blog.web.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public Map<String, Object> getCategories() {
        return ToolKit.assemblyResponseBody(categoryService.getCategories());
    }

    @GetMapping("/category/{categoryId}")
    public Map<String, Object> getCategory(@PathVariable String categoryId) {
        return ToolKit.assemblyResponseBody(categoryService.getCategory(categoryId));
    }

    @PutMapping("/category/{categoryId}")
    public Map<String, Object> updateCategory(@PathVariable String categoryId, @RequestBody @Valid CategoryDTO categoryDTO) {
        return ToolKit.assemblyResponseBody(categoryService.updateCategory(categoryId, categoryDTO));
    }

    @DeleteMapping("/category/{categoryId}")
    public Map<String, Object> deleteCategory(@PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ToolKit.emptyResponseBody();
    }
}
