package fr.bbougon.ousontmesaffaires.infrastructure.security;

public interface Encryptor {
    String cypher(byte[] dataToEncrypt);
}
