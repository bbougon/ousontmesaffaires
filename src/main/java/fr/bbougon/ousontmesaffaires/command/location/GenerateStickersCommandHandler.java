package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.infrastructure.pdf.PdfGenerator;
import org.apache.commons.lang3.tuple.Pair;

import javax.inject.Inject;
import java.io.File;

public class GenerateStickersCommandHandler implements CommandHandler<GenerateStickersCommand, File> {

    @Inject
    public GenerateStickersCommandHandler(final PdfGenerator pdfGenerator) {
        this.pdfGenerator = pdfGenerator;
    }

    @Override
    public Pair<File, Object> execute(final GenerateStickersCommand command) {
        return Pair.of(pdfGenerator.generate("", "content"), command);
    }

    private final PdfGenerator pdfGenerator;
}
