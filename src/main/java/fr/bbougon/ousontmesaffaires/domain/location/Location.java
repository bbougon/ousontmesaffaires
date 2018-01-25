package fr.bbougon.ousontmesaffaires.domain.location;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

public class Location {

    public Location() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public ImmutableList<Item> getItems() {
        return ImmutableList.<Item>builder().addAll(items).build();
    }

    public void add(final Item item) {
        items.add(item);
    }

    private final UUID id;
    private List<Item> items = Lists.newArrayList();
}
