package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import org.apache.commons.lang3.Validate;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.util.HashMap;

import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.IMAGE;
import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.TITLE;

public class DefaultPdfGenerator implements PdfGenerator {

    @Override
    public void generate(final Sticker sticker, final HashMap<StickerContent, String> content) {
        Validate.notEmpty(content, "The content cannot be empty.");
        validateImage(content);
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            addTitle(content, contentStream);
            addQrCode(content, document, contentStream);

            document.save(sticker.getContent());
            document.close();
        } catch (Exception e) {
        }
    }

    private void validateImage(final HashMap<StickerContent, String> content) {
        if(content.get(IMAGE) == null) {
            throw new IllegalArgumentException("The content must contain a qrcode image.");
        }
    }

    private void addQrCode(final HashMap<StickerContent, String> content, final PDDocument document, final PDPageContentStream contentStream) throws IOException {
        PDImageXObject image = PDImageXObject.createFromByteArray(document, new Codec().decodeBase64(content.get(IMAGE)), null);
        contentStream.drawImage(image, 100, 200);
        contentStream.close();
    }

    private void addTitle(final HashMap<StickerContent, String> content, final PDPageContentStream contentStream) throws IOException {
        if (content.get(TITLE) != null) {
            contentStream.setFont(PDType1Font.COURIER, 12);
            contentStream.beginText();
            contentStream.showText(content.get(TITLE));
            contentStream.endText();
        }
    }

}
