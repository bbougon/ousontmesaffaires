package fr.bbougon.ousontmesaffaires.web.helpers;

import org.apache.commons.codec.binary.Base64;

public class Encoder {

    public static String toBase64(String dataToEncode) {
        return Base64.encodeBase64URLSafeString(dataToEncode.getBytes());
    }
}
