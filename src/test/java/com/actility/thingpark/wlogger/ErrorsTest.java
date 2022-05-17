package com.actility.thingpark.wlogger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorsTest {

  @Test
  void getErrorMessage() {
    assertEquals(Errors.getErrorMessage(Errors.FORBIDDEN), Errors.messages[Errors.FORBIDDEN]);
    assertEquals(Errors.getErrorMessage(Errors.INVALID_QUERY_PARAMETERS,"parameter1"), String.format(Errors.messages[Errors.INVALID_QUERY_PARAMETERS],"parameter1"));
  }
}