package fr.bbougon.ousontmesaffaires.command.patch;

import fr.bbougon.ousontmesaffaires.domain.patch.DescriptionPatch;
import fr.bbougon.ousontmesaffaires.domain.patch.ImagePatch;
import fr.bbougon.ousontmesaffaires.domain.patch.Patch;
import fr.bbougon.ousontmesaffaires.domain.patch.PatchException;

public class Patches {

    static Patch getPatch(final String target, final String id, final Object data) {
        if (target.equals("item.description")) {
            return new DescriptionPatch(id, (String) data);

        }
        if (target.equals("item.image")) {
            return new ImagePatch(id, data);
        }
        throw new PatchException("UNKNOWN_PATCH", target);
    }
}
