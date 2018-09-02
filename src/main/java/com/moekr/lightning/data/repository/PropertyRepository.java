package com.moekr.lightning.data.repository;

import com.moekr.lightning.data.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, String> {
}
