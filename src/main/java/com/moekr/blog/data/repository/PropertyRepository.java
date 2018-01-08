package com.moekr.blog.data.repository;

import com.moekr.blog.data.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, String> {
}
