package com.actility.thingpark.wlogger.config;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptedStringConverterTest {

    @Test
    void convert() {
        EncryptedStringConverter c = new EncryptedStringConverter();
        assertThat(c.convert(null), is(nullValue()));
        assertThat(c.convert("clear:foobar"), hasToString("foobar"));
        assertThat(c.convert("clear:foo:bar"), hasToString("foo:bar"));
        assertThrows(RuntimeException.class, () -> c.convert("foobar"));
        assertThrows(RuntimeException.class, () -> c.convert("invalidKey:foobar"));
        assertThrows(RuntimeException.class, () -> c.convert("0:noHexChars"));
        assertThrows(RuntimeException.class, () -> c.convert("0:0123456789abcdef"));
        assertThat(c.convert("0:72CBB8E7DE2DC13B22A5B449016C4BA1"), hasToString("system1"));
    }
}