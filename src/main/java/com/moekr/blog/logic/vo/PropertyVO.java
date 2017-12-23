package com.moekr.blog.logic.vo;


import com.moekr.blog.data.entity.Property;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PropertyVO {
    private String id;
    private String name;
    private String value;

    public PropertyVO(Property property){
        BeanUtils.copyProperties(property, this);
    }
}
