package com.gatis.phonebook.service;

import com.gatis.phonebook.dto.PhoneDTO;
import com.gatis.phonebook.exception.PersonNotFoundException;
import com.gatis.phonebook.exception.PhoneNotFoundException;
import com.gatis.phonebook.model.Person;
import com.gatis.phonebook.model.Phone;
import com.gatis.phonebook.repository.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhoneService implements CrudService<Phone, PhoneDTO> {

  @Autowired PhoneRepository repository;
  @Autowired PersonService personService;

  @Override
  public List<Phone> getAll() {
    return repository.findAll();
  }

  @Override
  public Phone getOne(Long id) {
    return repository.findById(id).orElseThrow(() -> new PhoneNotFoundException(id));
  }

  @Override
  public Phone add(PhoneDTO phoneDTO) {
    Person person = personService.getOne(phoneDTO.getPersonId());
    return repository.save(new Phone(phoneDTO, person));
  }

  @Override
  public Phone replace(PhoneDTO phoneDTO, Long id) {
    return repository
        .findById(id)
        .map(
            phone -> {
              phone.setNumber(phoneDTO.getNumber());
              phone.setType(phoneDTO.getType());
              return repository.save(phone);
            })
        .orElseThrow(() -> new PhoneNotFoundException(id));
  }

  @Override
  public void delete(Long id) {
    try {
      repository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new PersonNotFoundException(id);
    }
  }
}
