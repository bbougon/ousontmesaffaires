package fr.bbougon.ousontmesaffaires.infrastructure.security;

public interface Encryptor {
    String encrypt(byte[] dataToEncrypt);
}
