package com.moekr.blog.logic.vo;

import com.moekr.blog.data.entity.Redirection;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class RedirectionVO {
    private String id;
    private String location;
    private int views;

    public RedirectionVO(Redirection redirection){
        BeanUtils.copyProperties(redirection, this);
    }
}
