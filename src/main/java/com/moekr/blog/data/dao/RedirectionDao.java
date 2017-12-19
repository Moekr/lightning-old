package com.moekr.blog.data.dao;

import com.moekr.blog.data.entity.Redirection;
import com.moekr.blog.data.repository.RedirectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RedirectionDao extends AbstractDao<Redirection, String>{
    RedirectionDao(RedirectionRepository repository) {
        super(repository);
    }
}
