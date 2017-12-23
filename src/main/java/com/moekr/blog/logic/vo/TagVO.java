package com.moekr.blog.logic.vo;

import com.moekr.blog.data.entity.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TagVO {
    private String id;
    private String name;

    public TagVO(Tag tag){
        BeanUtils.copyProperties(tag, this);
    }
}
