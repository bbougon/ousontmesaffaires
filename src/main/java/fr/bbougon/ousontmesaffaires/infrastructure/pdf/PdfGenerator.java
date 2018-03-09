package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import java.io.File;

public interface PdfGenerator {

    File generate(String filePath, String content);

}
