package com.moekr.lightning.logic.vo;


import com.moekr.lightning.data.entity.Property;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class PropertyVO implements Serializable {
    private String id;
    private String name;
    private String value;

    public PropertyVO(Property property) {
        BeanUtils.copyProperties(property, this);
    }
}
