package com.gatis.phonebook.model;

import com.gatis.phonebook.dto.PhoneDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phone")
public class Phone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 13)
  private String number;

  @Column(length = 6)
  private String type;

  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  private Person person;

  public Phone(PhoneDTO phone, Person person) {
    this.number = phone.getNumber();
    this.type = phone.getType();
    this.person = person;
  }

  public PhoneDTO toDTO() {
    return new PhoneDTO(person.getId(), number, type);
  }
}
