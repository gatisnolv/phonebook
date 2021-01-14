package com.gatis.phonebook.service;

import java.util.List;

public interface CrudService<T, S> {

  List<T> getAll();

  T getOne(Long id);

  T add(S dto);

  T replace(S dto, Long id);

  void delete(Long id);
}
