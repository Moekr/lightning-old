package com.moekr.lightning.data.repository;

import com.moekr.lightning.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String> {
}
