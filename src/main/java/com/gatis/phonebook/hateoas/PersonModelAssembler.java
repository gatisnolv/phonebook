package com.gatis.phonebook.hateoas;

import com.gatis.phonebook.controller.PersonController;
import com.gatis.phonebook.dto.PersonDTO;
import com.gatis.phonebook.model.Person;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.CollectionModel.of;
import static org.springframework.hateoas.EntityModel.of;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PersonModelAssembler
    implements RepresentationModelAssembler<Person, EntityModel<PersonDTO>> {
  @Override
  public EntityModel<PersonDTO> toModel(Person person) {
    return of(
        person.toDTO(),
        linkTo(methodOn(PersonController.class).getOne(person.getId())).withSelfRel(),
        linkTo(methodOn(PersonController.class).getAll()).withRel("person"));
  }

  @Override
  public CollectionModel<EntityModel<PersonDTO>> toCollectionModel(
      Iterable<? extends Person> persons) {
    return of(
        StreamSupport.stream(persons.spliterator(), false)
            .map(this::toModel)
            .collect(Collectors.toList()),
        linkTo(methodOn(PersonController.class).getAll()).withSelfRel());
  }
}
