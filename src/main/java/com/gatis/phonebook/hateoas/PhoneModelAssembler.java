package com.gatis.phonebook.hateoas;

import com.gatis.phonebook.controller.PhoneController;
import com.gatis.phonebook.dto.PhoneDTO;
import com.gatis.phonebook.model.Phone;
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
public class PhoneModelAssembler
    implements RepresentationModelAssembler<Phone, EntityModel<PhoneDTO>> {
  @Override
  public EntityModel<PhoneDTO> toModel(Phone phone) {
    return of(
        phone.toDTO(),
        linkTo(methodOn(PhoneController.class).getOne(phone.getId())).withSelfRel(),
        linkTo(methodOn(PhoneController.class).getAll()).withRel("phone"));
  }

  @Override
  public CollectionModel<EntityModel<PhoneDTO>> toCollectionModel(
      Iterable<? extends Phone> phones) {
    return of(
        StreamSupport.stream(phones.spliterator(), false)
            .map(this::toModel)
            .collect(Collectors.toList()),
        linkTo(methodOn(PhoneController.class).getAll()).withSelfRel());
  }
}
