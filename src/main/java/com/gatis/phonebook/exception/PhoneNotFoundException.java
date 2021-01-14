package com.gatis.phonebook.exception;

public class PhoneNotFoundException extends RuntimeException {

  public PhoneNotFoundException(Long id) {
    super("Could not find phone with id: " + id);
  }
}
