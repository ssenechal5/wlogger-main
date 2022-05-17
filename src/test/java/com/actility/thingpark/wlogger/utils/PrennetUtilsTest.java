package com.actility.thingpark.wlogger.utils;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrennetUtilsTest {

  @Test
  void dibrennanGantMestr() throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, DecoderException {
    assertEquals("system1", PrennetUtils.dibrennanGantMestr("72CBB8E7DE2DC13B22A5B449016C4BA1","0"));
  }

  @Test
  void prennanGantMestr() throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
    assertEquals("72CBB8E7DE2DC13B22A5B449016C4BA1", PrennetUtils.prennanGantMestr("system1","0"));
  }
}