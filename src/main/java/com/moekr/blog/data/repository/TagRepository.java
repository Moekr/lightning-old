package com.moekr.blog.data.repository;

import com.moekr.blog.data.entity.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, String> {
}
