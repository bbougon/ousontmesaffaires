package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import java.io.File;

public class PdfGeneratorForTest implements PdfGenerator {

    @Override
    public File generate(final String filePath, final String content) {
        return new File("src/test/resources/file/expected.pdf");
    }
}
