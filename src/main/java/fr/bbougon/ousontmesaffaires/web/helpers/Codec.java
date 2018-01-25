package fr.bbougon.ousontmesaffaires.web.helpers;

import org.apache.commons.codec.binary.Base64;

public class Codec {

    public String urlSafeToBase64(String dataToEncode) {
        return Base64.encodeBase64URLSafeString(dataToEncode.getBytes());
    }

    public String fromBase64(final String locationId) {
        return new String(Base64.decodeBase64(locationId.getBytes()));
    }

    public String toBase64(final byte[] binaryData) {
        return Base64.encodeBase64String(binaryData);
    }
}
