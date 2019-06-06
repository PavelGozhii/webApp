package dao;

import java.util.List;

public interface GenericDao<T> {
    boolean save(T t);

    T getById(String id);

    List<T> getByCriteria(String value, String field);

    boolean deleteByCriteria(String value, String field);

    boolean update(T t);

    boolean delete(String id);

    List<T> getAll();
}


