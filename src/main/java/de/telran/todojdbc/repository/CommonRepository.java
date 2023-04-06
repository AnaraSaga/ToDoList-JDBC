package de.telran.todojdbc.repository;

import java.util.Collection;

//for CRUD operations
public interface CommonRepository <T>{

    //save one entity - create, update
    public T save (T entity);

    //save iterable collection of entities - read
    public Iterable<T> saveAll (Collection<T> entities);

    //delete
    public void delete(T entity);

    //find entity by id - read operation
    public T findById(String id);

    //find all entities - read operation
    public Iterable<T> findAll();

}
