package fr.bbougon.ousontmesaffaires.domain.patch;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;

import java.util.function.Supplier;

public class DescriptionPatch extends PatchData<String> implements Patch<Description> {


    public DescriptionPatch(final String id, final String data) {
        super(id, data);
    }

    @Override
    public String getData() {
        return (String) data;
    }

    @Override
    public PatchedContainer<Description> apply(final Supplier<Container> supplier) {
        return supplier.get().updateDescription(getData());
    }
}
