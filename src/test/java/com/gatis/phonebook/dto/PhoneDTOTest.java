package com.gatis.phonebook.dto;

import com.gatis.phonebook.validation.AddPhone;
import com.gatis.phonebook.validation.ReplacePhone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneDTOTest {

  private static final String HOME = "home";
  private static final String MOBILE = "mobile";
  private static final String WORK = "work";
  private static final String VALID_NUMBER = "+371 12345678";

  private Validator validator;

  @BeforeEach
  public void setUp() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  void testAddRequiresAllFieldsNonNull() {
    PhoneDTO phone = new PhoneDTO(null, null, null);
    Set<ConstraintViolation<PhoneDTO>> violations = validator.validate(phone, AddPhone.class);
    assertEquals(3, violations.size());
  }

  @Test
  void testReplaceRequiresNumberAndTypeFieldsNonNull() {
    PhoneDTO phone = new PhoneDTO(null, null, null);
    Set<ConstraintViolation<PhoneDTO>> violations = validator.validate(phone, ReplacePhone.class);
    assertEquals(2, violations.size());
  }

  @Test
  void testIncorrectFormat() {
    PhoneDTO phone = new PhoneDTO(null, "+123 12345678", "business");
    Set<ConstraintViolation<PhoneDTO>> violations = validator.validate(phone, ReplacePhone.class);
    assertEquals(2, violations.size());
  }

  @Test
  void testCorrectFormat() {
    List<PhoneDTO> phones = new ArrayList<>();
    phones.add(new PhoneDTO(null, VALID_NUMBER, MOBILE));
    phones.add(new PhoneDTO(null, VALID_NUMBER, HOME));
    phones.add(new PhoneDTO(null, VALID_NUMBER, WORK));
    List<Set<ConstraintViolation<PhoneDTO>>> violationList =
        phones.stream().map(phone -> validator.validate(phone)).collect(Collectors.toList());
    violationList.forEach(violations -> assertTrue(violations.isEmpty()));
  }
}
