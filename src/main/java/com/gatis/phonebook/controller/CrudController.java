package com.gatis.phonebook.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface CrudController<T> {

  CollectionModel<EntityModel<T>> getAll();

  EntityModel<T> getOne(Long id);

  ResponseEntity<EntityModel<T>> add(T dto);

  EntityModel<T> replace(T dto, Long id);

  ResponseEntity<Object> delete(Long id);
}
