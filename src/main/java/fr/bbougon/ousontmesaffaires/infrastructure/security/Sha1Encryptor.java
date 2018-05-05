package fr.bbougon.ousontmesaffaires.infrastructure.security;

import org.apache.commons.codec.digest.DigestUtils;

public class Sha1Encryptor implements Encryptor {

    @Override
    public String encrypt(final byte[] dataToEncrypt) {
        return DigestUtils.sha1Hex(dataToEncrypt);
    }
}
