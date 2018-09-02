package com.moekr.lightning.logic.vo;

import com.moekr.lightning.data.entity.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class TagVO implements Serializable {
    private String id;
    private String name;

    public TagVO(Tag tag) {
        BeanUtils.copyProperties(tag, this);
    }
}
