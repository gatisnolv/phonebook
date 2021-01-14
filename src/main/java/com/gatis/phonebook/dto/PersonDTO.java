package com.gatis.phonebook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PersonDTO {
  @NotBlank(message = "A first name is mandatory")
  private String firstName;

  @NotBlank(message = "A last name is mandatory")
  private String lastName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  @Past(message = "The birth date must be in the past")
  @NotNull(message = "A birth date is mandatory")
  private LocalDate birthDate;

  private Set<PhoneDTO> phones;
}
