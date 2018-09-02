package com.moekr.lightning.data.dao;

import com.moekr.lightning.data.entity.Tag;
import com.moekr.lightning.data.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TagDAO extends AbstractDAO<Tag, String> {
    @Autowired
    public TagDAO(TagRepository repository) {
        super(repository);
    }
}
