package com.moekr.blog.logic.vo;

import com.moekr.blog.data.entity.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryVo {
    private String id;
    private String name;

    public CategoryVo(Category category){
        BeanUtils.copyProperties(category, this);
    }
}
