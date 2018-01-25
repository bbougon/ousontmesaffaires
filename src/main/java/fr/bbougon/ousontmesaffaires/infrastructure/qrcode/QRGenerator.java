package fr.bbougon.ousontmesaffaires.infrastructure.qrcode;

public interface QRGenerator {

    String encodeToBase64(final String content);

}
