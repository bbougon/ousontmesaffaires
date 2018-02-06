package fr.bbougon.ousontmesaffaires.domain.location;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

public class Location {

    Location() {
        id = UUID.randomUUID();
    }

    public Location(final String location, final Item item) {
        this();
        this.location = location;
        this.items.add(item);
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

    public String getLocation() {
        return location;
    }

    public static Location create(final String locationName, final Item item) {
        return new Location(locationName, item);
    }

    private final UUID id;
    private String location;
    private List<Item> items = Lists.newArrayList();
}
