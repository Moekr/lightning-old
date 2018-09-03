package com.moekr.lightning.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

abstract class AbstractDAO<T, ID extends Serializable> {
    private final JpaRepository<T, ID> repository;

    AbstractDAO(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    public T save(T entity) {
        return repository.save(entity);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findById(ID entityId) {
        return repository.findById(entityId).orElse(null);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }
}
