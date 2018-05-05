package fr.bbougon.ousontmesaffaires.infrastructure.security;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Sha1EncryptorTest {

    @Test
    public void canEncryptData() {
        assertThat(new Sha1Encryptor().encrypt("data-to-encrypt".getBytes())).isEqualTo("a4ffd1338480c91bb434f80b95bba98adf03d5e9");
    }
}