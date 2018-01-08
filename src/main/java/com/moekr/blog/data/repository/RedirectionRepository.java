package com.moekr.blog.data.repository;

import com.moekr.blog.data.entity.Redirection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedirectionRepository extends JpaRepository<Redirection, String> {
}
