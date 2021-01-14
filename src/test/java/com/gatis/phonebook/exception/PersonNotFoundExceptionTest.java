package com.gatis.phonebook.exception;

import org.junit.jupiter.api.Test;

import static com.gatis.phonebook.WebIntegrationTest.NON_EXISTING_PERSON_ID;
import static com.gatis.phonebook.WebIntegrationTest.NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonNotFoundExceptionTest {

  @Test
  void hasCorrectMessage() {
    Exception e = new PersonNotFoundException(NON_EXISTING_PERSON_ID);
    assertEquals(
        String.format(NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, "person") + NON_EXISTING_PERSON_ID,
        e.getMessage());
  }
}
