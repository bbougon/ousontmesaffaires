package fr.bbougon.ousontmesaffaires.infrastructure.pdf;

import fr.bbougon.ousontmesaffaires.command.sticker.Sticker;

import java.util.HashMap;

public interface PdfGenerator {

    void generate(Sticker sticker, HashMap<StickerContent, String> content);

}
