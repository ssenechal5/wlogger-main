package com.actility.common.dao;

import java.util.List;

public interface DaoLocal<E> {

  void persist(E entity);

  E findById(Object id);

  List<E> findAll();

  List<E> findAll(String orderedColumn);

  void delete(E entity);

  void flush();

  void clear();

}
