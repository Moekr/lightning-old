package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Tag;
import com.moekr.blog.data.repository.TagRepository;
import com.moekr.blog.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDao {
    private final TagRepository repository;

    @Autowired
    public TagDao(TagRepository repository) {
        this.repository = repository;
    }

    public Tag save(Tag tag){
        return repository.save(tag);
    }

    public List<Tag> findAll(){
        return ToolKit.iterableToList(repository.findAll());
    }

    public Tag findById(String tagId){
        return repository.findOne(tagId);
    }

    public void delete(Tag tag){
        repository.delete(tag);
    }
}
