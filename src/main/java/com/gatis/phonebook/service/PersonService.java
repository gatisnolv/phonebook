package com.gatis.phonebook.service;

import com.gatis.phonebook.dto.PersonDTO;
import com.gatis.phonebook.exception.PersonNotFoundException;
import com.gatis.phonebook.model.Person;
import com.gatis.phonebook.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService implements CrudService<Person, PersonDTO> {

  @Autowired PersonRepository repository;

  @Override
  public List<Person> getAll() {
    return repository.findAll();
  }

  @Override
  public Person getOne(Long id) {
    return repository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
  }

  @Override
  public Person add(PersonDTO personDTO) {
    return repository.save(new Person(personDTO));
  }

  @Override
  public Person replace(PersonDTO personDTO, Long id) {
    return repository
        .findById(id)
        .map(
            person -> {
              person.setFirstName(personDTO.getFirstName());
              person.setLastName(personDTO.getLastName());
              person.setBirthDate(personDTO.getBirthDate());
              return repository.save(person);
            })
        .orElseThrow(() -> new PersonNotFoundException(id));
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
