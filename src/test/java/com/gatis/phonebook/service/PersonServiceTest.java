package com.gatis.phonebook.service;

import com.gatis.phonebook.dto.PersonDTO;
import com.gatis.phonebook.exception.PersonNotFoundException;
import com.gatis.phonebook.model.Person;
import com.gatis.phonebook.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  private static final Long ID = 1L;

  @InjectMocks PersonService service;
  @Mock PersonRepository repository;
  @Captor ArgumentCaptor<Person> personCaptor;

  PersonDTO personDTO;
  Person person;

  @BeforeEach
  public void setup() {
    personDTO = new PersonDTO("George", "Harrison", LocalDate.now().minusDays(1), emptySet());
    person = new Person(personDTO);
  }

  @Test
  void getAll() {
    List<Person> persons = singletonList(person);
    when(repository.findAll()).thenReturn(persons);
    assertEquals(persons, service.getAll());
  }

  @Test
  void getOne() {
    when(repository.findById(ID)).thenReturn(ofNullable(person));
    assertEquals(person, service.getOne(ID));
  }

  @Test
  void add() {
    when(repository.save(any(Person.class))).thenReturn(person);
    assertEquals(person, service.add(personDTO));
  }

  @Test
  void replace() {
    when(repository.findById(ID)).thenReturn(ofNullable(person));
    when(repository.save(any(Person.class))).thenReturn(mock(Person.class));
    service.replace(personDTO, ID);
    verify(repository).save(personCaptor.capture());
    Person savedPerson = personCaptor.getValue();
    assertEquals(personDTO.getFirstName(), savedPerson.getFirstName());
    assertEquals(personDTO.getLastName(), savedPerson.getLastName());
    assertEquals(personDTO.getBirthDate(), savedPerson.getBirthDate());
  }

  @Test
  void delete() {
    service.delete(ID);
    verify(repository, times(1)).deleteById(ID);
  }

  @Test
  void deleteNonExistingThrowsException() {
    doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(ID);
    assertThrows(PersonNotFoundException.class, () -> service.delete(ID));
  }
}
