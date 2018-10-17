package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.ImmutableList;
import fr.bbougon.ousontmesaffaires.command.NextEvent;

import java.util.List;

public class PatchedContainer extends NextEvent<Container> {

    public PatchedContainer(final String description, final List<Item> items) {
        this.description = description;
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public ImmutableList<Item> getItems() {
        return ImmutableList.<Item>builder().addAll(items).build();
    }

    private String description;
    private List<Item> items;
}
