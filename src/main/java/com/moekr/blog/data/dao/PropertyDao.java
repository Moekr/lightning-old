package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Property;
import com.moekr.blog.data.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyDao extends AbstractDao<Property, String>{
    @Autowired
    public PropertyDao(PropertyRepository repository) {
        super(repository);
    }
}
