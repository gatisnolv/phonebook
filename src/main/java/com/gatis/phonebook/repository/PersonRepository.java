package com.gatis.phonebook.repository;

import com.gatis.phonebook.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}
