package fr.bbougon.ousontmesaffaires.command.patch;

import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.domain.patch.Patch;

public class PatchMapper {

    public PatchMapper() {
    }

    public Patch map(final String patch) {
        tempPatch = new Gson().fromJson(patch, TempPatch.class);
        return Patches.getPatch(tempPatch.target, tempPatch.id, tempPatch.data);
    }

    private TempPatch tempPatch;

    public static class TempPatch {

        String target;
        String id;
        int version;
        Object data;
    }
}
