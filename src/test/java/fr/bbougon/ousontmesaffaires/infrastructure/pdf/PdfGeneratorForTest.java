package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import java.io.File;
import java.util.HashMap;

public class PdfGeneratorForTest implements PdfGenerator {

    @Override
    public File generate(final String filePath, final HashMap<StickerContent, String> content) {
        return new File("src/test/resources/file/expected.pdf");
    }
}
