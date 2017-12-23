package com.moekr.blog.logic.vo;

import com.moekr.blog.data.entity.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CategoryVO {
    private String id;
    private String name;

    public CategoryVO(Category category){
        BeanUtils.copyProperties(category, this);
    }
}
