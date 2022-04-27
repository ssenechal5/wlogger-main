package com.actility.thingpark.wlogger.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrennetUtils {

  private static final Logger logger = Logger.getLogger(PrennetUtils.class.getName());

  private static final String PRENNET_ALWHETH = "AES";
  private static final String PRENNET_ALGORITHM = "AES/CBC/NoPadding";
  private static final String IVS = "56fe1a770a0011789ae7e9039b11d560";
  private static byte[] iv;
  private static IvParameterSpec ivspec;
  private static Cipher cypher;

  private static HashMap<String, String> keys = new HashMap<>();

  static {
    try {
      keys.put("0", "df8684ffc66d1a237c3924e7d801f7de");

      iv = DatatypeConverter.parseHexBinary(IVS);
      ivspec = new IvParameterSpec(iv);
      cypher = Cipher.getInstance(PRENNET_ALGORITHM);
    } catch (Exception e) {
      logger.log(Level.INFO,e.getMessage(),e);
    }
  }

  public static String dibrennanGantMestr(String value, String key)
          throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
          BadPaddingException, DecoderException {
    if (value == null) {
      throw new InvalidParameterException("value is null");
    }
    String cipherKey = keys.get(key);
    if (StringUtils.isBlank(cipherKey)) {
      throw new InvalidParameterException("Crypted Key not found for Version: " + key);
    }

    Key k = new SecretKeySpec(DatatypeConverter.parseHexBinary(cipherKey), PRENNET_ALWHETH);
    cypher.init(Cipher.DECRYPT_MODE, k, ivspec);

    return new String(
            Hex.decodeHex(
                DatatypeConverter.printHexBinary(
                        cypher.doFinal(DatatypeConverter.parseHexBinary(value)))
                    .toCharArray()))
        .trim();
  }

  public static String prennanGantMestr(String string, String key)
      throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
          BadPaddingException {

    String cipherKey = keys.get(key);
    if (StringUtils.isBlank(cipherKey)) {
      logger.severe("Crypted Key not found for Version :" + key);
      return null;
    }

    Key k = new SecretKeySpec(DatatypeConverter.parseHexBinary(cipherKey), PRENNET_ALWHETH);
    cypher.init(Cipher.ENCRYPT_MODE, k, ivspec);

    String passHex = Hex.encodeHexString(string.getBytes());
    int n = ((int) Math.ceil((float) passHex.length() / 32)) * 32;

    return DatatypeConverter.printHexBinary(
        cypher.doFinal(DatatypeConverter.parseHexBinary(StringUtils.rightPad(passHex, n, "0"))));
  }
}
