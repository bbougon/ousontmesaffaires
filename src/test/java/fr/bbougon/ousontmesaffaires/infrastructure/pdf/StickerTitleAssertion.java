package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class StickerTitleAssertion {

    public void assertThatDocumentHasTitle(final PDDocument document, final String title) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(document);
        assertThat(text).contains(title);
    }
}
