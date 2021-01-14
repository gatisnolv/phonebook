package com.gatis.phonebook.controller;

import com.gatis.phonebook.dto.PersonDTO;
import com.gatis.phonebook.hateoas.PersonModelAssembler;
import com.gatis.phonebook.model.Person;
import com.gatis.phonebook.service.PersonService;
import com.gatis.phonebook.util.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.gatis.phonebook.WebIntegrationTest.*;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
@WithMockUser(username = "root", password = "password")
class PersonControllerTest {
  private static final Long ID = 1L;
  private static final String CONTROLLER_ROOT = "/person";
  private static final String PATH_SEPARATOR = "/";

  @Autowired MockMvc mvc;
  @MockBean PersonService service;
  @SpyBean PersonModelAssembler assembler;
  PersonDTO personDTO;
  Person person;
  JsonMapper mapper;

  @BeforeEach
  public void setup() {
    personDTO = new PersonDTO("George", "Harrison", LocalDate.now().minusDays(1), emptySet());
    person = new Person(personDTO);
    mapper = new JsonMapper();
  }

  @Test
  void getAllPersons() throws Exception {
    when(service.getAll()).thenReturn(singletonList(person));
    mvc.perform(get(CONTROLLER_ROOT).accept(HAL_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath(PREFIX + PERSON_LIST, hasSize(1)))
        .andExpect(
            jsonPath(
                PREFIX + PERSON_LIST + FIRST_ELEMENT + FIRST_NAME, is(personDTO.getFirstName())))
        .andExpect(
            jsonPath(PREFIX + PERSON_LIST + FIRST_ELEMENT + LAST_NAME, is(personDTO.getLastName())))
        .andExpect(
            jsonPath(
                PREFIX + PERSON_LIST + FIRST_ELEMENT + BIRTH_DATE,
                is(personDTO.getBirthDate().toString())))
        .andExpect(jsonPath(PREFIX + PERSON_LIST + FIRST_ELEMENT + PHONES, is(notNullValue())));
  }

  @Test
  void getPerson() throws Exception {
    when(service.getOne(ID)).thenReturn(person);
    mvc.perform(get(CONTROLLER_ROOT + PATH_SEPARATOR + ID).accept(HAL_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath(PREFIX + FIRST_NAME, is(personDTO.getFirstName())))
        .andExpect(jsonPath(PREFIX + LAST_NAME, is(personDTO.getLastName())))
        .andExpect(jsonPath(PREFIX + BIRTH_DATE, is(personDTO.getBirthDate().toString())))
        .andExpect(jsonPath(PREFIX + PHONES, is(notNullValue())));
  }

  @Test
  void addPerson() throws Exception {
    person.setId(1L);
    when(service.add(ArgumentMatchers.any(PersonDTO.class))).thenReturn(person);
    mvc.perform(
            post(CONTROLLER_ROOT)
                .with(csrf())
                .accept(HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.toJson(personDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(PREFIX + FIRST_NAME, is(personDTO.getFirstName())))
        .andExpect(jsonPath(PREFIX + LAST_NAME, is(personDTO.getLastName())))
        .andExpect(jsonPath(PREFIX + BIRTH_DATE, is(personDTO.getBirthDate().toString())))
        .andExpect(jsonPath(PREFIX + PHONES, is(notNullValue())));
  }

  @Test
  void replacePerson() throws Exception {
    when(service.replace(ArgumentMatchers.any(PersonDTO.class), eq(ID))).thenReturn(person);
    mvc.perform(
            put(CONTROLLER_ROOT + PATH_SEPARATOR + ID)
                .with(csrf())
                .accept(HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.toJson(personDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(PREFIX + FIRST_NAME, is(personDTO.getFirstName())))
        .andExpect(jsonPath(PREFIX + LAST_NAME, is(personDTO.getLastName())))
        .andExpect(jsonPath(PREFIX + BIRTH_DATE, is(personDTO.getBirthDate().toString())))
        .andExpect(jsonPath(PREFIX + PHONES, is(notNullValue())));
  }

  @Test
  void deletePerson() throws Exception {
    mvc.perform(delete(CONTROLLER_ROOT + PATH_SEPARATOR + ID).with(csrf()).accept(HAL_JSON))
        .andExpect(status().isNoContent());
  }
}
