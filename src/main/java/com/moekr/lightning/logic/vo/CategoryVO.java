package com.moekr.lightning.logic.vo;

import com.moekr.lightning.data.entity.Category;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class CategoryVO implements Serializable {
    private String id;
    private String name;
    private int level;
    private boolean visible;

    public CategoryVO(Category category) {
        BeanUtils.copyProperties(category, this);
    }
}
