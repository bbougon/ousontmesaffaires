package fr.bbougon.ousontmesaffaires.domain.patch;

import fr.bbougon.ousontmesaffaires.domain.container.Container;

public class Description {
    public Description(final Container container) {
        this.container = container;
    }

    public String getDescription() {
        return container.getDescription();
    }

    private Container container;
}
