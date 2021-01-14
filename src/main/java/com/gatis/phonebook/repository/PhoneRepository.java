package com.gatis.phonebook.repository;

import com.gatis.phonebook.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {}
