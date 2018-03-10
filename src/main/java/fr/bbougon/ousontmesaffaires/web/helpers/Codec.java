package fr.bbougon.ousontmesaffaires.web.helpers;

import org.apache.commons.codec.binary.Base64;

import java.util.UUID;

public class Codec {

    public UUID fromString(final String uuid) {
        return UUID.fromString(uuid);
    }

    public String urlSafeToBase64(String dataToEncode) {
        return Base64.encodeBase64URLSafeString(dataToEncode.getBytes());
    }

    public String fromBase64(final String dataToDecode) {
        return new String(Base64.decodeBase64(dataToDecode.getBytes()));
    }

    public String toBase64(final byte[] binaryData) {
        return Base64.encodeBase64String(binaryData);
    }

    public byte[] decodeBase64(final String dataToDecode) {
        return Base64.decodeBase64(dataToDecode);
    }
}
