package com.actility.thingpark.wlogger.controller;

import com.actility.thingpark.wlogger.exception.WloggerException;
import com.actility.thingpark.wlogger.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StoresControllerTest {

  private StoreService storeService;
  private StoresController stores;
  private UriInfo uriInfo;

  private String domain = "domain";
  private String locale = "en-US";
  private String type = "module";

  @BeforeEach
  void setUp() {
    this.storeService = mock(StoreService.class);
    this.stores = new StoresController();
    this.stores.inject(this.storeService);

    this.uriInfo = mock(UriInfo.class);
    when(uriInfo.getRequestUri())
        .thenReturn(
            UriBuilder.fromUri("https://www.example.com/thingpark/wlogger/rest/portal?baz=foo")
                .build());
  }

  private static Stream<Arguments> provideArgumentsDecoder() {
    return Stream.of(Arguments.of("0", false), Arguments.of("1", true));
  }

  @ParameterizedTest(name = "run decoder => (decType :{0})")
  @MethodSource("provideArgumentsDecoder")
  void decoder(String decType, boolean expectedLte) throws WloggerException, IOException {
    when(this.storeService.getDecoder(domain, locale, expectedLte)).thenReturn("decoder");
    String data = this.stores.decoder(null, uriInfo, domain, type, locale, decType);
    assertEquals("decoder", data);
    verify(this.storeService, times(1)).getDecoder(domain, locale, expectedLte);
  }

  private static Stream<Arguments> provideDataArgumentsSubtype() {
    return Stream.of(
        Arguments.of(0, 0, false, false), Arguments.of(1, 0, true, false), Arguments.of(0, 1, false, true), Arguments.of(1, 1, true, true));
  }

  @ParameterizedTest(name = "run subtype => (itype :{0}, np :{1})")
  @MethodSource("provideDataArgumentsSubtype")
  void subtype(Integer itype, Integer np, boolean expectedLte, boolean expectedNp) throws WloggerException, IOException {
    when(this.storeService.getSubtype(domain, locale, expectedLte, expectedNp)).thenReturn("subtype");
    String data = this.stores.subtype(null, uriInfo, domain, type, locale, itype, np);
    assertEquals("subtype", data);
  }
}
