package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Tag;
import com.moekr.blog.data.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TagDao extends AbstractDao<Tag, String>{
    @Autowired
    public TagDao(TagRepository repository) {
        super(repository);
    }
}
