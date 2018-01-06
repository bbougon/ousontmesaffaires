package fr.bbougon.ousontmesaffaires.web.helpers;


import org.apache.commons.codec.binary.Base64;

public class Decoder {
    public static String fromBase64(final String locationId) {
        return new String(Base64.decodeBase64(locationId.getBytes()));
    }
}
