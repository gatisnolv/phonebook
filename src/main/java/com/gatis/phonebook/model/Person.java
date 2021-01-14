package com.gatis.phonebook.model;

import com.gatis.phonebook.dto.PersonDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 20)
  private String firstName;

  @Column(length = 20)
  private String lastName;

  private LocalDate birthDate;

  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
  private Set<Phone> phones = new HashSet<>();

  public Person(PersonDTO person) {
    this.firstName = person.getFirstName();
    this.lastName = person.getLastName();
    this.birthDate = person.getBirthDate();
  }

  public PersonDTO toDTO() {
    return new PersonDTO(
        firstName,
        lastName,
        birthDate,
        phones.stream().map(Phone::toDTO).collect(Collectors.toSet()));
  }
}
