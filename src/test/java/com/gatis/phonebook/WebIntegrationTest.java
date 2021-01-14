package com.gatis.phonebook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class WebIntegrationTest {

  public static final Long NON_EXISTING_PERSON_ID = 3L;
  public static final Long NON_EXISTING_PHONE_ID = 4L;
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String BIRTH_DATE = "birthDate";
  public static final String PHONES = "phones";
  public static final String PERSON_ID = "personId";
  public static final String NUMBER = "number";
  public static final String TYPE = "type";

  public static final String NOT_FOUND_EXCEPTION_MESSAGE_TEMPLATE = "Could not find %s with id: ";
  public static final String PREFIX = "$.";
  public static final String PERSON_LIST = "_embedded.personDTOList";
  public static final String PHONE_LIST = "_embedded.phoneDTOList";
  public static final String AGGREGATE_ACCESSOR = "[*].";
  public static final String FIRST_ELEMENT = "[0].";
  public static final String SECOND_ELEMENT = "[1].";
  public static final String FIRST_PERSON_LOCATOR = PREFIX + PERSON_LIST + FIRST_ELEMENT;
  public static final String SECOND_PERSON_LOCATOR = PREFIX + PERSON_LIST + SECOND_ELEMENT;

  private static final String PERSON_CONTROLLER_ROOT = "/person";
  private static final String FIRST_PERSONS_FIRST_NAME = "George";
  private static final String FIRST_PERSONS_LAST_NAME = "Harrison";
  private static final String FIRST_PERSONS_BIRTH_DATE = "1943-02-25";
  private static final String FIRST_PERSONS_FIRST_NUMBER = "+371 12345678";
  private static final String FIRST_PERSONS_SECOND_NUMBER = "+371 23456789";
  private static final String SECOND_PERSONS_FIRST_NAME = "Ringo";
  private static final String SECOND_PERSONS_LAST_NAME = "Starr";
  private static final String SECOND_PERSONS_BIRTH_DATE = "1940-07-07";
  private static final String SECOND_PERSONS_NUMBER = "+371 87654321";

  @Autowired WebTestClient rest;

  @Test
  void testCorrectResultsFromDatabase() {
    WebTestClient.BodyContentSpec body =
        rest.get()
            .uri(PERSON_CONTROLLER_ROOT)
            .headers(headers -> headers.setBasicAuth("root", "password"))
            .accept(HAL_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(HAL_JSON)
            .expectBody();
    body.jsonPath(FIRST_PERSON_LOCATOR + FIRST_NAME)
        .value(is(FIRST_PERSONS_FIRST_NAME))
        .jsonPath(FIRST_PERSON_LOCATOR + LAST_NAME)
        .value(is(FIRST_PERSONS_LAST_NAME))
        .jsonPath(FIRST_PERSON_LOCATOR + BIRTH_DATE)
        .value(is(FIRST_PERSONS_BIRTH_DATE))
        .jsonPath(FIRST_PERSON_LOCATOR + PHONES + AGGREGATE_ACCESSOR + NUMBER)
        .value(containsInAnyOrder(FIRST_PERSONS_FIRST_NUMBER, FIRST_PERSONS_SECOND_NUMBER))
        .jsonPath(SECOND_PERSON_LOCATOR + FIRST_NAME)
        .value(is(SECOND_PERSONS_FIRST_NAME))
        .jsonPath(SECOND_PERSON_LOCATOR + LAST_NAME)
        .value(is(SECOND_PERSONS_LAST_NAME))
        .jsonPath(SECOND_PERSON_LOCATOR + BIRTH_DATE)
        .value(is(SECOND_PERSONS_BIRTH_DATE))
        .jsonPath(SECOND_PERSON_LOCATOR + PHONES + FIRST_ELEMENT + NUMBER)
        .value(is(SECOND_PERSONS_NUMBER));
  }

  @Test
  void testUnauthorizedStatusWhenNoAuthenticationPresent() {
    rest.get().uri(PERSON_CONTROLLER_ROOT).exchange().expectStatus().isUnauthorized();
  }
}
