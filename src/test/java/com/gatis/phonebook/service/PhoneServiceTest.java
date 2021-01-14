package com.gatis.phonebook.service;

import com.gatis.phonebook.dto.PhoneDTO;
import com.gatis.phonebook.exception.PersonNotFoundException;
import com.gatis.phonebook.model.Phone;
import com.gatis.phonebook.repository.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneServiceTest {

  private static final Long ID = 1L;

  @InjectMocks PhoneService service;
  @Mock PhoneRepository repository;
  @Mock PersonService personService;
  @Captor ArgumentCaptor<Phone> phoneCaptor;

  PhoneDTO phoneDTO;
  Phone phone;

  @BeforeEach
  public void setup() {
    phoneDTO = new PhoneDTO(ID, "+371 12345678", "mobile");
    phone = new Phone(phoneDTO, null);
  }

  @Test
  void getAll() {
    List<Phone> phones = singletonList(phone);
    when(repository.findAll()).thenReturn(phones);
    assertEquals(phones, service.getAll());
  }

  @Test
  void getOne() {
    when(repository.findById(ID)).thenReturn(ofNullable(phone));
    assertEquals(phone, service.getOne(ID));
  }

  @Test
  void add() {
    when(personService.getOne(anyLong())).thenReturn(null);
    when(repository.save(any(Phone.class))).thenReturn(phone);
    assertEquals(phone, service.add(phoneDTO));
  }

  @Test
  void replace() {
    when(repository.findById(ID)).thenReturn(ofNullable(phone));
    when(repository.save(any(Phone.class))).thenReturn(mock(Phone.class));
    service.replace(phoneDTO, ID);
    verify(repository).save(phoneCaptor.capture());
    Phone savedPhone = phoneCaptor.getValue();
    assertEquals(phoneDTO.getNumber(), savedPhone.getNumber());
    assertEquals(phoneDTO.getType(), savedPhone.getType());
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
