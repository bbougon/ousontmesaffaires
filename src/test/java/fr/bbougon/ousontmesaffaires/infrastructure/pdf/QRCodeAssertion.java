package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.junit.rules.TemporaryFolder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class QRCodeAssertion {

    private TemporaryFolder temporaryFolder = new TemporaryFolder();

    QRCodeAssertion() {
        try {
            temporaryFolder.create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assertThatDocumentHasContent(final String qrCodeContent, final PDDocument document) throws IOException {
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
                        assertThat(new MultiFormatReader().decode(binaryBitmap).getText()).isEqualTo(qrCodeContent);
                    }
                }
            } catch (Exception e) {
                fail("Exception has been thrown before assert. Message : %s", e.getMessage());
            }
        });
    }
}
