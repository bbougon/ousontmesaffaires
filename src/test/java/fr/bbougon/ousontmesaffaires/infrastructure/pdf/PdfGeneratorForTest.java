package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PdfGeneratorForTest implements PdfGenerator {

    @Override
    public void generate(Sticker sticker, final HashMap<StickerContent, String> content) {
        try {
            sticker.setContent(FileUtils.readFileToByteArray(new File("src/test/resources/file/expected.pdf")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
