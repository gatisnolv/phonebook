package com.gatis.phonebook.controller;

import com.gatis.phonebook.dto.PhoneDTO;
import com.gatis.phonebook.hateoas.PhoneModelAssembler;
import com.gatis.phonebook.service.PhoneService;
import com.gatis.phonebook.validation.AddPhone;
import com.gatis.phonebook.validation.ReplacePhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;

@RestController
@RequestMapping("phone")
public class PhoneController implements CrudController<PhoneDTO> {

  @Autowired PhoneService service;
  @Autowired PhoneModelAssembler assembler;

  @GetMapping(produces = HAL_JSON_VALUE)
  public CollectionModel<EntityModel<PhoneDTO>> getAll() {
    return assembler.toCollectionModel(service.getAll());
  }

  @GetMapping(value = "/{id}", produces = HAL_JSON_VALUE)
  public EntityModel<PhoneDTO> getOne(@PathVariable Long id) {
    return assembler.toModel(service.getOne(id));
  }

  @PostMapping(produces = HAL_JSON_VALUE)
  public ResponseEntity<EntityModel<PhoneDTO>> add(
      @Validated(AddPhone.class) @RequestBody PhoneDTO phoneDTO) {
    EntityModel<PhoneDTO> entityModel = assembler.toModel(service.add(phoneDTO));
    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @PutMapping(value = "/{id}", produces = HAL_JSON_VALUE)
  public EntityModel<PhoneDTO> replace(
      @Validated(ReplacePhone.class) @RequestBody PhoneDTO phoneDTO, @PathVariable Long id) {
    return assembler.toModel(service.replace(phoneDTO, id));
  }

  @DeleteMapping(value = "/{id}", produces = HAL_JSON_VALUE)
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
