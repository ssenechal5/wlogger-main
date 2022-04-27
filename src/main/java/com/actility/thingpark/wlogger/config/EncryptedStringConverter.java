package com.actility.thingpark.wlogger.config;

import com.actility.thingpark.wlogger.utils.PrennetUtils;
import org.apache.commons.codec.DecoderException;
import org.eclipse.microprofile.config.spi.Converter;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Objects;

/**
 * A converter that produces values of type {@link EncryptedString}.
 * <p>
 * Value must be formatted as {keyVersion}:{encryptedValue}.
 * Unencrypted value may be provided with clear:{value}.
 */
public class EncryptedStringConverter implements Converter<EncryptedString>, Serializable {

    private static final long serialVersionUID = -566191897225686615L;

    public static final String CLEAR = "clear";

    @Override
    public EncryptedString convert(String s) {
        if (s == null) {
            return null;
        }
        final String[] components = s.split(":", 2);
        if (components.length != 2) {
            throw new IllegalArgumentException(
                    String.format("Error decrypting '%s'. Value must be composed of a key version and encrypted value separated by a colon " +
                            "or prefixed by 'CLEAR:' for an unencrypted value.", s)
            );
        }
        final String keyVersion = components[0];
        final String encryptedValue = components[1];
        return decrypt(keyVersion, encryptedValue);
    }

    private EncryptedString decrypt(String keyVersion, String encryptedValue) {
        if (Objects.equals(CLEAR, keyVersion)) {
            return new EncryptedString(encryptedValue);
        }
        try {
            final String value = PrennetUtils.dibrennanGantMestr(encryptedValue, keyVersion);
            // dibrennanGantMestr return null when key is not found
            return new EncryptedString(value);
        } catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException
                | DecoderException e) {
            throw new IllegalArgumentException("Error decrypting value", e);
        }
    }
}
