package com.actility.thingpark.wlogger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecodedHistoryTest {

  DecodedLoraHistory.Builder builder = DecodedLoraHistory.builder();

  DecodedLoraHistory decodedLoraHistory;
  DecodedLoraHistory decodedLoraHistoryWithoutRecipients;
  DecodedLoraHistory decodedLoraHistoryError;

  @BeforeEach
  void setUp() {
    List<Recipient> recipients = new ArrayList<>();
    recipients.add(Recipient.builder().withId("id").withStatus("Ok").withDestinations(null).build());
    decodedLoraHistory = builder.withAsReportDeliveryID("AsReportDeliveryID").withAsRecipients(recipients).build();
    decodedLoraHistoryWithoutRecipients = builder.withAsReportDeliveryID("AsReportDeliveryID").withAsRecipients(null).build();
    List<Recipient> recipients2 = new ArrayList<>();
    recipients2.add(Recipient.builder().withId("id").withStatus("Ok").withDestinations(null).build());
    recipients2.add(Recipient.builder().withId("id2").withStatus("Error").withDestinations(null).build());
    decodedLoraHistoryError = builder.withAsReportDeliveryID("AsReportDeliveryID").withAsRecipients(recipients2).build();
  }

  @Test
  void getDeliveryStatus() {
    assertEquals("Ok",decodedLoraHistory.getDeliveryStatus());
    assertEquals("Ok",decodedLoraHistoryWithoutRecipients.getDeliveryStatus());
    assertEquals("Error",decodedLoraHistoryError.getDeliveryStatus());
  }

  @Test
  void getRecipientListAsHtmlTable() {
    assertEquals("<table class='chainList'><thead><tr><td>AS ID</td><td>Status</td><td>Transmission errors</td></tr></thead><tbody><tr><td>id</td><td>Ok</td><td>None</td></tr></tbody></table>",decodedLoraHistory.getRecipientListAsHtmlTable());
    assertEquals("",decodedLoraHistoryWithoutRecipients.getRecipientListAsHtmlTable());
    assertEquals(
        "<table class='chainList'><thead><tr><td>AS ID</td><td>Status</td><td>Transmission errors</td></tr></thead><tbody><tr><td>id</td><td>Ok</td><td>None</td></tr><tr><td>id2</td><td>Error</td><td>None</td></tr></tbody></table>",
        decodedLoraHistoryError.getRecipientListAsHtmlTable());
  }

  @Test
  void getRecipientListAsElements() {
    assertEquals(1,decodedLoraHistory.getRecipientListAsElements().size());
    assertEquals(null,decodedLoraHistoryWithoutRecipients.getRecipientListAsElements());
    assertEquals(2,decodedLoraHistoryError.getRecipientListAsElements().size());
  }

  @Test
  void getRecipientListAsCsv() {
    List<String> list = decodedLoraHistory.getRecipientListAsCsv(5);
    assertEquals("id",list.get(0));
    assertEquals("Ok",list.get(1));
    assertEquals("",list.get(2));
    list = decodedLoraHistoryWithoutRecipients.getRecipientListAsCsv(5);
    assertEquals("",list.get(0));
    assertEquals("",list.get(1));
    assertEquals("",list.get(2));
    list = decodedLoraHistoryError.getRecipientListAsCsv(5);
    assertEquals("id",list.get(0));
    assertEquals("Ok",list.get(1));
    assertEquals("",list.get(2));
    assertEquals("id2",list.get(3));
    assertEquals("Error",list.get(4));
    assertEquals("",list.get(5));
  }
}