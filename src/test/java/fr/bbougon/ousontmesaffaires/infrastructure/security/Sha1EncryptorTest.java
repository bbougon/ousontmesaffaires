package fr.bbougon.ousontmesaffaires.infrastructure.security;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Sha1EncryptorTest {

    @Test
    public void canCypherData() {
        assertThat(new Sha1Encryptor().cypher("data-to-cypher".getBytes())).isEqualTo("7e000d163f5b1fa42cafbaf077bff944ddd07cdf");
    }
}