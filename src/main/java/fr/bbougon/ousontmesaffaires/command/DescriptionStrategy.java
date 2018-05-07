package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.domain.container.Container;

import java.util.function.Supplier;

public class DescriptionStrategy implements Strategy {

    @Override
    public void apply(final Patch patch, final Supplier supplier) {
        Container container = (Container) supplier.get();
        container.setDescription((String) patch.getData());
    }
}
