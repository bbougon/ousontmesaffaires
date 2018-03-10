package fr.bbougon.ousontmesaffaires.command.location;

import com.google.common.collect.Maps;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;

public class GenerateStickersCommandHandler implements CommandHandler<GenerateStickersCommand, File> {

    @Inject
    public GenerateStickersCommandHandler(final PdfGenerator pdfGenerator, final QRGenerator qrCodeGenerator) {
        this.pdfGenerator = pdfGenerator;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public Pair<File, Object> execute(final GenerateStickersCommand command) {
        Location location = Repositories.locationRepository().findById(command.getUUID());
        if(location == null) {
            return Pair.of(null, null);
        }
        return Pair.of(pdfGenerator.generate(getPdfName(location), getPdfContent(command, location)), command);
    }

    private HashMap<String, String> getPdfContent(final GenerateStickersCommand command, final Location location) {
        HashMap<String, String> content = Maps.newHashMap();
        content.put(TITLE, location.getLocation());
        content.put(IMAGE, qrCodeGenerator.encodeToBase64(command.getLocationUrl()));
        return content;
    }

    private String getPdfName(final Location location) {
        return location.getLocation().replace(" ", "_") + ".pdf";
    }

    private final PdfGenerator pdfGenerator;
    private final QRGenerator qrCodeGenerator;
    private static final String TITLE = "title";
    private static final String IMAGE = "image";
}
