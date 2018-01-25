package fr.bbougon.ousontmesaffaires.infrastructure.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRGeneratorEngine implements QRGenerator {

    @Inject
    public QRGeneratorEngine(final Codec codec) {
        this.codec = codec;
    }

    @Override
    public String encodeToBase64(final String content) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, FORMAT, stream);
            return codec.toBase64(stream.toByteArray());
        } catch (WriterException | IOException e) {
            return null;
        }
    }

    private final Codec codec;
    private static final String FORMAT = "png";
    private static final int WIDTH = 150;
    private static final int HEIGHT = 150;
}
