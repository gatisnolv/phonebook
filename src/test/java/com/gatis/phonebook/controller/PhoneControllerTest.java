package com.gatis.phonebook.controller;

import com.gatis.phonebook.dto.PhoneDTO;
import com.gatis.phonebook.hateoas.PhoneModelAssembler;
import com.gatis.phonebook.model.Person;
import com.gatis.phonebook.model.Phone;
import com.gatis.phonebook.service.PhoneService;
import com.gatis.phonebook.util.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.gatis.phonebook.WebIntegrationTest.*;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhoneController.class)
@WithMockUser(username = "root", password = "password")
class PhoneControllerTest {
  private static final Long ID = 1L;
  private static final String CONTROLLER_ROOT = "/phone";
  private static final String PATH_SEPARATOR = "/";

  @Autowired MockMvc mvc;

  @MockBean PhoneService service;
  @SpyBean PhoneModelAssembler assembler;
  PhoneDTO phoneDTO;
  Phone phone;
  Person person;
  JsonMapper mapper;

  @BeforeEach
  public void setup() {
    phoneDTO = new PhoneDTO(ID, "+371 12345678", "mobile");
    person = new Person();
    person.setId(ID);
    phone = new Phone(phoneDTO, person);
    phone.setId(ID);
    mapper = new JsonMapper();
  }

  @Test
  void getAllPhones() throws Exception {
    when(service.getAll()).thenReturn(singletonList(phone));
    mvc.perform(get(CONTROLLER_ROOT).accept(HAL_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath(PREFIX + PHONE_LIST, hasSize(1)))
        .andExpect(
            jsonPath(
                PREFIX + PHONE_LIST + FIRST_ELEMENT + PERSON_ID,
                equalTo(phoneDTO.getPersonId()),
                Long.class))
        .andExpect(jsonPath(PREFIX + PHONE_LIST + FIRST_ELEMENT + NUMBER, is(phoneDTO.getNumber())))
        .andExpect(jsonPath(PREFIX + PHONE_LIST + FIRST_ELEMENT + TYPE, is(phoneDTO.getType())));
  }

  @Test
  void getPhone() throws Exception {
    when(service.getOne(ID)).thenReturn(phone);
    mvc.perform(get(CONTROLLER_ROOT + PATH_SEPARATOR + ID).accept(HAL_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath(PREFIX + PERSON_ID, is(phoneDTO.getPersonId()), Long.class))
        .andExpect(jsonPath(PREFIX + NUMBER, is(phoneDTO.getNumber())))
        .andExpect(jsonPath(PREFIX + TYPE, is(phoneDTO.getType())));
  }

  @Test
  void addPhone() throws Exception {
    when(service.add(ArgumentMatchers.any(PhoneDTO.class))).thenReturn(phone);
    mvc.perform(
            post(CONTROLLER_ROOT)
                .with(csrf())
                .accept(HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.toJson(phoneDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(PREFIX + PERSON_ID, is(phoneDTO.getPersonId()), Long.class))
        .andExpect(jsonPath(PREFIX + NUMBER, is(phoneDTO.getNumber())))
        .andExpect(jsonPath(PREFIX + TYPE, is(phoneDTO.getType())));
  }

  @Test
  void replacePhone() throws Exception {
    when(service.replace(ArgumentMatchers.any(PhoneDTO.class), eq(ID))).thenReturn(phone);
    mvc.perform(
            put(CONTROLLER_ROOT + PATH_SEPARATOR + ID)
                .with(csrf())
                .accept(HAL_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.toJson(phoneDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(PREFIX + PERSON_ID, is(phoneDTO.getPersonId()), Long.class))
        .andExpect(jsonPath(PREFIX + NUMBER, is(phoneDTO.getNumber())))
        .andExpect(jsonPath(PREFIX + TYPE, is(phoneDTO.getType())));
  }

  @Test
  void deletePhone() throws Exception {
    mvc.perform(delete(CONTROLLER_ROOT + PATH_SEPARATOR + ID).with(csrf()).accept(HAL_JSON))
        .andExpect(status().isNoContent());
  }
}
