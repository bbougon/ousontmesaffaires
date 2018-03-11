package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class PdfGeneratorForTest implements PdfGenerator {

    @Override
    public void generate(Sticker sticker, final HashMap<StickerContent, String> content) {
        try {
            final InputStream inputStream = FileUtils.openInputStream(new File("src/test/resources/file/expected.pdf"));
            IOUtils.copy(inputStream, sticker.getContent());
            inputStream.close();
            sticker.getContent().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
