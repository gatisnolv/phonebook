package com.gatis.phonebook.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ExceptionMapper {

  private static final String EXCEPTIONS = "exceptions";
  private static final String ERROR = "Error";

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({MethodArgumentNotValidException.class})
  public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, List<String>> errors = new HashMap<>();
    List<String> validationErrors =
        ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());
    errors.put(EXCEPTIONS, validationErrors);
    log.error(ERROR, ex);
    return errors;
  }

  @ResponseStatus(BAD_REQUEST)
  @ExceptionHandler({HttpMessageNotReadableException.class})
  public Map<String, List<String>> handleParsingExceptions(HttpMessageNotReadableException ex) {
    Map<String, List<String>> error = new HashMap<>();
    error.put(EXCEPTIONS, Collections.singletonList(ex.getMessage()));
    log.error(ERROR, ex);
    return error;
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({PersonNotFoundException.class, PhoneNotFoundException.class})
  public Map<String, List<String>> handleNotFoundExceptions(RuntimeException ex) {
    Map<String, List<String>> error = new HashMap<>();
    error.put(EXCEPTIONS, Collections.singletonList(ex.getMessage()));
    log.error(ERROR, ex);
    return error;
  }
}
