package fr.bbougon.ousontmesaffaires.command.sticker;

import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.util.HashMap;

import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.IMAGE;
import static fr.bbougon.ousontmesaffaires.infrastructure.pdf.StickerContent.TITLE;

public class GenerateStickersCommandHandler implements CommandHandler<GenerateStickersCommand, Sticker> {

    @Inject
    public GenerateStickersCommandHandler(final PdfGenerator pdfGenerator, final QRGenerator qrCodeGenerator) {
        this.pdfGenerator = pdfGenerator;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public Pair<Sticker, Object> execute(final GenerateStickersCommand command) {
        Location location = Repositories.locationRepository().findById(command.getUUID());
        if(location == null) {
            return Pair.of(null, null);
        }
        Sticker sticker = new Sticker(location.getLocation().replace(" ", "_") + ".pdf");
        pdfGenerator.generate(sticker, getPdfContent(command, location));
        return Pair.of(sticker, command);
    }

    private HashMap<StickerContent, String> getPdfContent(final GenerateStickersCommand command, final Location location) {
        HashMap<StickerContent, String> content = Maps.newHashMap();
        content.put(TITLE, location.getLocation());
        content.put(IMAGE, qrCodeGenerator.encodeToBase64(command.getLocationUrl()));
        return content;
    }

    private final PdfGenerator pdfGenerator;
    private final QRGenerator qrCodeGenerator;
}
