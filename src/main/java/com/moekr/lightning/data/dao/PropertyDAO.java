package com.moekr.lightning.data.dao;

import com.moekr.lightning.data.entity.Property;
import com.moekr.lightning.data.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyDAO extends AbstractDAO<Property, String> {
    @Autowired
    public PropertyDAO(PropertyRepository repository) {
        super(repository);
    }
}
