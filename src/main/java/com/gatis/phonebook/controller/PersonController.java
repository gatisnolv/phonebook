package com.gatis.phonebook.controller;

import com.gatis.phonebook.dto.PersonDTO;
import com.gatis.phonebook.hateoas.PersonModelAssembler;
import com.gatis.phonebook.model.Person;
import com.gatis.phonebook.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@RestController
@RequestMapping("person")
public class PersonController implements CrudController<PersonDTO> {

  @Autowired PersonService service;
  @Autowired PersonModelAssembler assembler;

  @GetMapping(produces = HAL_JSON_VALUE)
  public CollectionModel<EntityModel<PersonDTO>> getAll() {
    return assembler.toCollectionModel(service.getAll());
  }

  @GetMapping(value = "/{id}", produces = HAL_JSON_VALUE)
  public EntityModel<PersonDTO> getOne(@PathVariable Long id) {
    return assembler.toModel(service.getOne(id));
  }

  @PostMapping(produces = HAL_JSON_VALUE)
  public ResponseEntity<EntityModel<PersonDTO>> add(@Valid @RequestBody PersonDTO personDTO) {
    Person person = service.add(personDTO);
    EntityModel<PersonDTO> entityModel = assembler.toModel(person);
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @PutMapping(value = "/{id}", produces = HAL_JSON_VALUE)
  public EntityModel<PersonDTO> replace(
      @Valid @RequestBody PersonDTO personDTO, @PathVariable Long id) {
    return assembler.toModel(service.replace(personDTO, id));
  }

  @DeleteMapping(value = "/{id}", produces = HAL_JSON_VALUE)
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
