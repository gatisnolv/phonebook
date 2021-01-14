package com.gatis.phonebook.exception;

import org.junit.jupiter.api.Test;

import static com.gatis.phonebook.WebIntegrationTest.NON_EXISTING_PHONE_ID;
import static com.gatis.phonebook.WebIntegrationTest.NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PhoneNotFoundExceptionTest {

  @Test
  void hasCorrectMessage() {
    Exception e = new PhoneNotFoundException(NON_EXISTING_PHONE_ID);
    assertEquals(
        String.format(NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, "phone") + NON_EXISTING_PHONE_ID,
        e.getMessage());
  }
}
