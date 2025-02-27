package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Identifiable;
import java.util.Iterator;

public interface GenericRepository<T extends Identifiable> {
    T create(T item);
    Iterator<T> findAll();
    T findById(String id);
    T update(String id, T updatedItem);
    void delete(String id);
}