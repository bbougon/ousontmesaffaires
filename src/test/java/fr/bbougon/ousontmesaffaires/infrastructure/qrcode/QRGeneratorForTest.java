package fr.bbougon.ousontmesaffaires.infrastructure.qrcode;

public class QRGeneratorForTest implements QRGenerator {

    @Override
    public String encodeToBase64(final String content) {
        return content;
    }
}
