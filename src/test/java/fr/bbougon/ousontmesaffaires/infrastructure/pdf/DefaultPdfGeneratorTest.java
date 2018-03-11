package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import com.google.common.collect.Maps;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorEngine;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.IMAGE;
import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.TITLE;
import static org.assertj.core.api.Assertions.*;

public class DefaultPdfGeneratorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

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

        PDDocument document = PDDocument.load(sticker.getContent().toByteArray());
        assertTitle("A title", document);
        assertQrCode("My content", document);
    }

    @Test
    public void canGeneratePdfIfNoTitleGiven() throws Exception {
        HashMap<StickerContent, String> content = Maps.newHashMap();
        content.put(IMAGE, new QRGeneratorEngine().encodeToBase64("My content"));

        new DefaultPdfGenerator().generate(sticker, content);

        PDDocument document = PDDocument.load(sticker.getContent().toByteArray());
        assertQrCode("My content", document);
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

    }

    private void assertQrCode(final String qrCodeContent, final PDDocument document) throws IOException {
        PDPage page = document.getPage(0);
        new PDFStreamParser(page).parse();
        PDResources resources = page.getResources();
        resources.getXObjectNames().forEach(cosName -> {
            try {
                if (cosName != null) {
                    PDXObject image = resources.getXObject(cosName);
                    if (image instanceof PDImageXObject) {
                        File actual = temporaryFolder.newFile("actual.png");
                        ImageIO.write(((PDImageXObject) image).getImage(), "png", actual);
                        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                                new BufferedImageLuminanceSource(
                                        ImageIO.read(actual))));
                        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
                        String text1 = qrCodeResult.getText();
                        assertThat(text1).isEqualTo(qrCodeContent);
                    }
                }
            } catch (Exception e) {
                fail("Exception has been thrown before assert. Message : %s", e.getMessage());
            }
        });
    }

    private void assertTitle(final String title, final PDDocument document) throws IOException {
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(document);
        assertThat(text).contains(title);
    }

    private Sticker sticker;
}