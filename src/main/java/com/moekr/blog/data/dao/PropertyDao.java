package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Property;
import com.moekr.blog.data.repository.PropertyRepository;
import com.moekr.blog.util.ToolKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PropertyDao {
    private final PropertyRepository repository;

    @Autowired
    public PropertyDao(PropertyRepository repository) {
        this.repository = repository;
    }

    public Property save(Property property){
        return repository.save(property);
    }

    public List<Property> findAll(){
        return ToolKit.iterableToList(repository.findAll());
    }

    public Property findById(String propertyId){
        return repository.findOne(propertyId);
    }
}
