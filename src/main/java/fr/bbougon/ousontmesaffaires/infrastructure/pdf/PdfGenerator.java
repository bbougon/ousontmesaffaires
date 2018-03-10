package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import java.io.File;
import java.util.HashMap;

public interface PdfGenerator {

    File generate(String filePath, HashMap<StickerContent, String> content);

}
