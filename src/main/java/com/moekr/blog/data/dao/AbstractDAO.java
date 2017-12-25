package com.moekr.blog.data.dao;

import com.moekr.blog.util.ToolKit;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;

abstract class AbstractDAO<T, ID extends Serializable> {
    private final CrudRepository<T, ID> repository;

    AbstractDAO(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public List<T> findAll() {
        return ToolKit.iterableToList(repository.findAll());
    }

    public T findById(ID entityId) {
        return repository.findOne(entityId);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }
}
