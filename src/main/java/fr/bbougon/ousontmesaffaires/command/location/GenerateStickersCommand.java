package fr.bbougon.ousontmesaffaires.command.location;

import fr.bbougon.ousontmesaffaires.command.Command;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;

import java.io.File;

public class GenerateStickersCommand implements Command<File> {
    public GenerateStickersCommand(final Codec codec, final String locationId) {
    }
}
