package com.gatis.phonebook.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.gatis.phonebook.WebIntegrationTest.NON_EXISTING_PHONE_ID;
import static com.gatis.phonebook.WebIntegrationTest.NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionMapperTest {

  private static final String MESSAGE = "message";
  private static final String EXCEPTIONS = "exceptions";

  @Test
  void notFoundExceptionMapping() {
    PhoneNotFoundException e = new PhoneNotFoundException(NON_EXISTING_PHONE_ID);
    ExceptionMapper mapper = new ExceptionMapper();
    assertTrue(
        mapper
            .handleNotFoundExceptions(e)
            .get(EXCEPTIONS)
            .contains(
                String.format(NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE, "phone")
                    + NON_EXISTING_PHONE_ID));
  }

  @Test
  void httpMessageNotReadableExceptionMapping() {
    HttpMessageNotReadableException e =
        new HttpMessageNotReadableException(MESSAGE, (HttpInputMessage) null);
    ExceptionMapper mapper = new ExceptionMapper();
    assertFalse(mapper.handleParsingExceptions(e).get(EXCEPTIONS).isEmpty());
  }

  @Test
  void validationExceptionMapping() {
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock(BindingResult.class);
    ObjectError error = new ObjectError("name", MESSAGE);
    when(exception.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getAllErrors()).thenReturn(singletonList(error));
    ExceptionMapper mapper = new ExceptionMapper();
    assertTrue(mapper.handleValidationExceptions(exception).get(EXCEPTIONS).contains(MESSAGE));
  }
}
