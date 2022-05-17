package com.actility.thingpark.wlogger.model;

import com.actility.thingpark.wlogger.dto.Element;
import com.actility.thingpark.wlogger.dto.ElementDestinations;
import com.actility.thingpark.wlogger.dto.ElementRecipients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RecipientTest {

  Recipient recipient;

  Recipient recipientDropped;

  Recipient recipientWithoutDestinations;

  Recipient recipientWithoutDestinationsDropped;

  @BeforeEach
  void setUp() {

    Recipient.Builder builder = Recipient.builder();
    Recipient.Destination.Builder destinationBuilder = Recipient.Destination.builder();
    List<Recipient.Destination> destinations = new ArrayList<>();

    destinationBuilder.withIdx(0).withUrl("http://foo.foo.fr").withStatus("Ok");
    destinations.add(destinationBuilder.build());

    destinationBuilder.withIdx(1).withUrl("http://foo.foo.fr/lora").withStatus("Error").withErrorMessage("HTTP 404");
    destinations.add(destinationBuilder.build());

    builder.withId("Id").withStatus("Ok").withDestinations(destinations);
    recipient = builder.build();

    builder.withId("Id").withStatus("Ok").withDestinations(null);
    recipientWithoutDestinations = builder.build();

    builder.withId("Id").withStatus("Ok").withDestinations(destinations);
    recipientDropped = builder.build();

    builder.withId("Id").withStatus("Ok").withDestinations(null);
    recipientWithoutDestinationsDropped = builder.build();
  }

  @Test
  void getAsElement() {
    Optional<ElementRecipients> elementRecipients = recipient.getAsElement();
    assertEquals(true,elementRecipients.isPresent());

    ElementRecipients element = elementRecipients.get();
    assertEquals("Id",element.getId());
    assertEquals("Ok",element.getStatus());
    assertEquals(2,element.getDestinations().size());

    ElementDestinations elementDestinations = ((ElementDestinations) element.getDestinations().get(0));
    assertEquals(0,elementDestinations.getIdx());
    assertEquals("Ok",elementDestinations.getStatus());
    assertEquals("http://foo.foo.fr",elementDestinations.getUrl());
    assertNull(elementDestinations.getErrorMessage());

    elementDestinations = ((ElementDestinations) element.getDestinations().get(1));
    assertEquals(1,elementDestinations.getIdx());
    assertEquals("Error",elementDestinations.getStatus());
    assertEquals("http://foo.foo.fr/lora",elementDestinations.getUrl());
    assertEquals("HTTP 404",elementDestinations.getErrorMessage());

  }

  @Test
  void getAsElementWithoutDestinations() {
    Optional<ElementRecipients> elementRecipients = recipientWithoutDestinations.getAsElement();
    assertEquals(true,elementRecipients.isPresent());

    ElementRecipients element = elementRecipients.get();
    assertEquals("Id",element.getId());
    assertEquals("Ok",element.getStatus());
    assertNull(element.getDestinations());
  }

  @Test
  void getAsCsv() {
    assertEquals(3,recipient.getAsCsv().size());
  }

  @Test
  void getAsCsvWithoutDestinations() {
    assertEquals(3,recipientWithoutDestinations.getAsCsv().size());
  }

  @Test
  void getEmptyCsv() {
    assertEquals(3,recipient.getEmptyCsv().size());
  }

  @Test
  void getDestinationsAsElements() {
    List<Element> list = recipient.getDestinationsAsElements();

    ElementDestinations elementDestinations = ((ElementDestinations) list.get(0));
    assertEquals(0,elementDestinations.getIdx());
    assertEquals("Ok",elementDestinations.getStatus());
    assertEquals("http://foo.foo.fr",elementDestinations.getUrl());
    assertNull(elementDestinations.getErrorMessage());

    elementDestinations = ((ElementDestinations) list.get(1));
    assertEquals(1,elementDestinations.getIdx());
    assertEquals("Error",elementDestinations.getStatus());
    assertEquals("http://foo.foo.fr/lora",elementDestinations.getUrl());
    assertEquals("HTTP 404",elementDestinations.getErrorMessage());
  }

  @Test
  void getDestinationsAsElementsWithoutDestinations() {
    assertNull(recipientWithoutDestinations.getDestinationsAsElements());
  }

  @Test
  void getDestinationsAsCsv() {
    assertEquals(
        "[{\"idx\":0,\"url\":\"http://foo.foo.fr\",\"status\":\"Ok\",\"errorMessage\":null},{\"idx\":1,\"url\":\"http://foo.foo.fr/lora\",\"status\":\"Error\",\"errorMessage\":\"HTTP 404\"}]",
        recipient.getDestinationsAsCsv());
  }

  @Test
  void getAsHtmlRow() {
    assertEquals(
        "<tr><td>Id</td><td>Ok</td><td> Idx: 0<br/> Url: http://foo.foo.fr<br/> Status: Ok</br>------------------------------------------------------------</br> Idx: 1<br/> Url: http://foo.foo.fr/lora<br/> Status: Error<br/> Error: HTTP 404</td></tr>",
        recipient.getAsHtmlRow());
  }
/*
  @Test
  void getAsHtmlRowDropped() {
    assertEquals(
            "<tr><td>Id</td><td>Ok</td><td>Discarded</td></tr>",
            recipientDropped.getAsHtmlRow());
  }
*/
  @Test
  void getDestinationsAsCsvWithoutDestinations() {
    assertEquals(
            "",
            recipientWithoutDestinations.getDestinationsAsCsv());
  }

  @Test
  void getAsHtmlRowWithoutDestinations() {
    assertEquals(
            "<tr><td>Id</td><td>Ok</td><td>None</td></tr>",
            recipientWithoutDestinations.getAsHtmlRow());
  }
/*
  @Test
  void getAsHtmlRowWithoutDestinationsDropped() {
    assertEquals(
            "<tr><td>Id</td><td>Ok</td><td>Discarded</td></tr>",
            recipientWithoutDestinationsDropped.getAsHtmlRow());
  }
*/
}