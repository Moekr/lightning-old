package com.moekr.blog.logic.vo;


import com.moekr.blog.data.entity.Property;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PropertyVo {
    private String id;
    private String name;
    private String value;

    public PropertyVo(Property property){
        BeanUtils.copyProperties(property, this);
    }
}
