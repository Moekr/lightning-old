package com.moekr.lightning.data.dao;

import com.moekr.lightning.data.entity.Redirection;
import com.moekr.lightning.data.repository.RedirectionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RedirectionDAO extends AbstractDAO<Redirection, String> {
    RedirectionDAO(RedirectionRepository repository) {
        super(repository);
    }
}
