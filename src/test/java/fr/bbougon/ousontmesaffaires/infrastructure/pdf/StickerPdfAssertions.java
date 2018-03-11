package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public class StickerPdfAssertions {

    public static void assertQrCode(final String qrCodeContent, final PDDocument document) throws IOException {
        new QRCodeAssertion().assertThatDocumentHasContent(qrCodeContent, document);
    }

    public static void assertTitle(final String title, final PDDocument document) throws IOException {
        new StickerTitleAssertion().assertThatDocumentHasTitle(document, title);
    }
}
