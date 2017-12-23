package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Redirection;
import com.moekr.blog.data.repository.RedirectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RedirectionDAO extends AbstractDAO<Redirection, String> {
    RedirectionDAO(RedirectionRepository repository) {
        super(repository);
    }
}
