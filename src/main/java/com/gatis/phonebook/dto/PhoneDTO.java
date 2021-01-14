package com.gatis.phonebook.dto;

import com.gatis.phonebook.validation.AddPhone;
import com.gatis.phonebook.validation.ReplacePhone;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class PhoneDTO {
  private static final String NUMBER_REGEX = "\\+371 [0-9]{8}";
  private static final String PHONE_TYPE_REGEX = "home|work|mobile";

  @NotNull(message = "A personId is mandatory when adding a new phone", groups = AddPhone.class)
  private Long personId;

  @Pattern(
      message = "The number must match " + NUMBER_REGEX,
      regexp = NUMBER_REGEX,
      groups = {ReplacePhone.class, AddPhone.class})
  @NotBlank(
      message = "A number is mandatory",
      groups = {ReplacePhone.class, AddPhone.class})
  private String number;

  @Pattern(
      message = "The phone type must match " + PHONE_TYPE_REGEX,
      regexp = PHONE_TYPE_REGEX,
      groups = {ReplacePhone.class, AddPhone.class})
  @NotBlank(
      message = "A phone type is mandatory",
      groups = {ReplacePhone.class, AddPhone.class})
  private String type;
}
