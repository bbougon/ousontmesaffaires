package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import ch.qos.logback.classic.Level;
import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorEngine;
import fr.bbougon.ousontmesaffaires.test.utils.TestAppender;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.IMAGE;
import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class DefaultPdfGeneratorTest {

    @Before
    public void before() {
        sticker = new Sticker("file.pdf");
    }

    @Test
    public void canGeneratePdf() throws Exception {
        HashMap<StickerContent, String> content = Maps.newHashMap();
        content.put(TITLE, "A title");
        content.put(IMAGE, new QRGeneratorEngine().encodeToBase64("My content"));

        new DefaultPdfGenerator().generate(sticker, content);

        PDDocument document = PDDocument.load(sticker.getContent());
        StickerPdfAssertions.assertTitle("A title", document);
        StickerPdfAssertions.assertQrCode("My content", document);
    }

    @Test
    public void canGeneratePdfIfNoTitleGiven() throws Exception {
        HashMap<StickerContent, String> content = Maps.newHashMap();
        content.put(IMAGE, new QRGeneratorEngine().encodeToBase64("My content"));

        new DefaultPdfGenerator().generate(sticker, content);

        PDDocument document = PDDocument.load(sticker.getContent());
        StickerPdfAssertions.assertQrCode("My content", document);
    }

    @Test
    public void canNotGeneratePdfWithEmptyContent() {
        try {
            new DefaultPdfGenerator().generate(sticker, Maps.newHashMap());
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("The content cannot be empty.");
        }
    }

    @Test
    public void canNotGeneratePdfIfQrcodeNotPresent() {
        try {
            HashMap<StickerContent, String> content = Maps.newHashMap();
            content.put(TITLE, "A title");
            new DefaultPdfGenerator().generate(sticker, content);
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("The content must contain a qrcode image.");
        }
    }

    @Test
    public void handleExceptionsInPdfGeneration() {
        try {
            HashMap<StickerContent, String> content = Maps.newHashMap();
            content.put(TITLE, "A title");
            content.put(IMAGE, new QRGeneratorEngine().encodeToBase64("My content"));
            Sticker sticker = new Sticker("new pdf.pdf") {
                @Override
                public void setContent(final byte[] content) {
                    throw new RuntimeException("error");
                }
            };
            new DefaultPdfGenerator().generate(sticker, content);
            failBecauseExceptionWasNotThrown(PdfGenerationException.class);
        } catch (PdfGenerationException e) {
            assertThat(e.getMessage()).isEqualTo("Error during PDF generation (new pdf.pdf)");
            assertThat(TestAppender.hasMessageInLevel(Level.WARN, "Error during PDF generation (new pdf.pdf)"));
        }

    }

    private Sticker sticker;
}