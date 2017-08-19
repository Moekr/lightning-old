package com.moekr.blog.data.repository;

import com.moekr.blog.data.entity.Property;
import org.springframework.data.repository.CrudRepository;

public interface PropertyRepository extends CrudRepository<Property, String> {
}
