package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.decoder.Decoder;
import com.actility.thingpark.wlogger.model.DecodedLoraHistory;
import com.actility.thingpark.wlogger.model.DecodedLteHistory;
import com.actility.thingpark.wlogger.model.Direction;
import com.actility.thingpark.wloggerwrap.WLWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.actility.thingpark.wlogger.service.CSVService.LRR_MAX;
import static com.actility.thingpark.wlogger.service.CSVService.RECIPIENT_MAX;
import static com.actility.thingpark.wlogger.service.DecoderService.DECODER_AUTOMATIC;
import static com.actility.thingpark.wlogger.service.DecoderService.DECODER_RAW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CSVServiceTest {
  private static final String SOME_DECODER = "SOME_DECODER";

  private DecoderService decoderMock;
  private CSVService csvService;
  private static WLWrapper someWrapper =
      new WLWrapper() {
        @Override
        public void setDecodedMessage(Object message, Map<String, String> properties) {
          throw new RuntimeException("Not implemented");
        }

        @Override
        public Pair<String, String> getLocation() {
          throw new RuntimeException("Not implemented");
        }

        @Override
        public boolean isManagedLocation() {
          throw new RuntimeException("Not implemented");
        }

        @Override
        public String getLine() {
          throw new RuntimeException("Not implemented");
        }

        @Override
        public String getCSVHeader(String delim) {
          return String.join(delim, new String[] {"1", "2", "3"});
        }

        @Override
        public String getCSVLine(String delim) {
          return String.join(delim, new String[] {"1", "2", "3"});
        }
      };

  private Decoder someDecoder =
      new Decoder() {
        @Override
        public Object decode(String payloadHex, Map<String, String> properties) {
          throw new RuntimeException("Not implemented");
        }

        @Override
        public String encode(Object payload, Map<String, String> properties) {
          throw new RuntimeException("Not implemented");
        }
      };

  @BeforeEach
  void setUp() {
    this.decoderMock = mock(DecoderService.class);
    this.csvService = new CSVService(decoderMock);
  }

  @Test
  void getHeadersDeviceHistoryLte() {
    String[] headers = CSVService.getHeadersDeviceHistoryLte();
    assertNotNull(headers);
    assertEquals(26 + 3 * RECIPIENT_MAX, headers.length);
  }

  private static Stream<Arguments> provideGetHeaderLrrArguments() {
    return Stream.of(
        Arguments.of("LRR[0] key", 0, "key"),
        Arguments.of("LRR[12] property", 12, "property"),
        Arguments.of("LRR[465789] azerty", 465789, "azerty"));
  }

  @ParameterizedTest(name = "run getHeaderLrr({1}, {2})")
  @MethodSource("provideGetHeaderLrrArguments")
  void getHeaderLrr(String expected, int i, String key) {
    assertEquals(expected, CSVService.getHeaderLrr(i, key));
  }

  private static Stream<Arguments> provideGetHeadersLrrArguments() {
    return Stream.of(Arguments.of(12, true), Arguments.of(7, false));
  }

  @ParameterizedTest(name = "run getHeadersLrr(0, {1})")
  @MethodSource("provideGetHeadersLrrArguments")
  void getHeadersLrr(int expectedSize, boolean passiveRoaming) {
    List<String> headers = CSVService.getHeadersLrr(0, passiveRoaming);
    assertNotNull(headers);
    assertEquals(expectedSize, headers.size());
  }

  private static Stream<Arguments> provideGetHeadersDeviceHistoryLoraArguments() {
    return Stream.of(
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 5 + 2 + 1  + 3 * RECIPIENT_MAX, DECODER_AUTOMATIC, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 5 + 2 + 1 + 3 * RECIPIENT_MAX, DECODER_AUTOMATIC, false),
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 3 + 2 + 1 + 3 * RECIPIENT_MAX, SOME_DECODER, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 3 + 2 + 1 + 3 * RECIPIENT_MAX, SOME_DECODER, false),
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, DECODER_RAW, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, DECODER_RAW, false),
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, null, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, null, false));
  }

  @ParameterizedTest(name = "run getHeadersDeviceHistoryLora({1}, {2})")
  @MethodSource("provideGetHeadersDeviceHistoryLoraArguments")
  void getHeadersDeviceHistoryLora(int expectedLength, String decoder, boolean passiveRoaming) throws Exception {
    if (SOME_DECODER.equals(decoder)) {
      when(decoderMock.getWLWrapper(eq(decoder))).thenReturn(someWrapper);
    }
    String[] headers = csvService.getHeadersDeviceHistoryLora(decoder, passiveRoaming);
    assertNotNull(headers);
    assertEquals(expectedLength, headers.length);
    if (SOME_DECODER.equals(decoder)) {
      verify(decoderMock, times(1)).getWLWrapper(eq(decoder));
    } else {
      verify(decoderMock, never()).getWLWrapper(eq(decoder));
    }
  }

  @Test
  void convertDeviceHistoryLteToCsvLine() {
    String[] values =
        CSVService.convertDeviceHistoryLteToCsvLine(DecodedLteHistory.builder().build());
    assertNotNull(values);
    assertEquals(26  + 3 * RECIPIENT_MAX, values.length);
  }

  private static Stream<Arguments> provideConvertDeviceHistoryLoraToCsvLineArguments() {

    DecodedLoraHistory histo = DecodedLoraHistory.builder()
            .withDirection(Direction.UPLINK)
            .withPayloadHex("01234567")
            .build();
    histo.setWrapper(someWrapper);

    return Stream.of(
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 5 + 2 + 1 + 3 * RECIPIENT_MAX, DecodedLoraHistory.builder().build(), DECODER_AUTOMATIC, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 5 + 2 + 1 +  3 * RECIPIENT_MAX, DecodedLoraHistory.builder().build(), DECODER_AUTOMATIC, false),
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, histo, SOME_DECODER, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, histo, SOME_DECODER, false),
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, DecodedLoraHistory.builder().build(), DECODER_RAW, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, DecodedLoraHistory.builder().build(), DECODER_RAW, false),
        Arguments.of(26 + LRR_MAX * 12 + 19 + 5 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, DecodedLoraHistory.builder().build(), null, true),
        Arguments.of(26 + LRR_MAX * 7 + 19 + 28 + 2 + 1 + 3 * RECIPIENT_MAX, DecodedLoraHistory.builder().build(), null, false));
  }

  @ParameterizedTest(name = "run convertDeviceHistoryLoraToCsvLine({2}, {3})")
  @MethodSource("provideConvertDeviceHistoryLoraToCsvLineArguments")
  void convertDeviceHistoryLoraToCsvLine(
      int expectedLength,
      DecodedLoraHistory decodedLoraHistory,
      String decoder,
      boolean passiveRoaming) throws Exception {
    if (SOME_DECODER.equals(decoder)) {
      when(decoderMock.getWLWrapper(eq(decoder))).thenReturn(someWrapper);
      when(decoderMock.getDecoder(eq(decoder))).thenReturn(someDecoder);
    }
    String[] values =
        csvService.convertDeviceHistoryLoraToCsvLine(decodedLoraHistory, decoder, passiveRoaming);
    assertNotNull(values);
    assertEquals(expectedLength, values.length);
    if (SOME_DECODER.equals(decoder)) {
      verify(decoderMock, never()).getWLWrapper(eq(decoder));
      verify(decoderMock, never()).getDecoder(eq(decoder));
    } else {
      verify(decoderMock, never()).getWLWrapper(eq(decoder));
      verify(decoderMock, never()).getDecoder(eq(decoder));
    }
  }

  @Test
  void writeLTECsv() throws IOException {
    Writer writer = new StringWriter();
    csvService.writeLTECsv(
        writer,
        ',',
        Arrays.asList(
            DecodedLteHistory.builder().build(),
            DecodedLteHistory.builder().build(),
            DecodedLteHistory.builder().build()));
    assertNotNull(writer.toString());
    String[] lines = writer.toString().split("\n", -1);
    assertEquals(5, lines.length);
    assertEquals("", lines[4]);
  }

  @Test
  void writeLoraCsv() throws IOException {
    Writer writer = new StringWriter();
    csvService.writeLoraCsv(
        writer,
        ',',
        Arrays.asList(
            DecodedLoraHistory.builder().build(),
            DecodedLoraHistory.builder().build(),
            DecodedLoraHistory.builder().build()),
        DECODER_AUTOMATIC,
        true);
    assertNotNull(writer.toString());
    String[] lines = writer.toString().split("\n", -1);
    assertEquals(5, lines.length);
    assertEquals("", lines[4]);
  }
}
