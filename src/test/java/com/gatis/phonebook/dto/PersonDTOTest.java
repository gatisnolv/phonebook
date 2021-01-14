package com.gatis.phonebook.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonDTOTest {
  private static final String FIRST_NAME = "George";
  private static final String LAST_NAME = "Harrison";

  private Validator validator;

  @BeforeEach
  public void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void testRequiresFieldsNonNull() {
    PersonDTO person = new PersonDTO(null, null, null, null);
    Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);
    assertEquals(3, violations.size());
  }

  @Test
  void testForbiddenDate() {
    PersonDTO person = new PersonDTO(FIRST_NAME, LAST_NAME, LocalDate.now(), null);
    Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);
    assertEquals(1, violations.size());
  }

  @Test
  void testCorrectFormat() {
    PersonDTO person = new PersonDTO(FIRST_NAME, LAST_NAME, LocalDate.now().minusDays(1), null);
    Set<ConstraintViolation<PersonDTO>> violations = validator.validate(person);
    assertTrue(violations.isEmpty());
  }
}
