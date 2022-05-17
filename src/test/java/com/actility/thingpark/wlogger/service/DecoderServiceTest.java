package com.actility.thingpark.wlogger.service;

import com.actility.thingpark.wlogger.engine.EngineClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class DecoderServiceTest {

    private EngineClient engineMock;
    private DecoderService decoderService;

    @BeforeEach
    void setUp() {
        this.engineMock = mock(EngineClient.class);
        this.decoderService = new DecoderService(engineMock);
    }

    @Test
    void parseCustomerDate() {
        assertEquals(Optional.empty(), decoderService.parseCustomerData(null));
        assertEquals(Optional.empty(), decoderService.parseCustomerData(""));
        assertEquals(Optional.empty(), decoderService.parseCustomerData("{}"));
        assertEquals(Optional.empty(), decoderService.parseCustomerData("{foobar"));
        assertEquals(Optional.empty(), decoderService.parseCustomerData("{\"loc\": \"foobar\"}"));
    }
}