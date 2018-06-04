package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.AggregateRoot;

import java.util.List;
import java.util.UUID;

public class Container extends AggregateRoot {

    Container() {
        super(UUID.randomUUID());
    }

    private Container(final String name, final Item item) {
        this();
        this.name = name;
        this.items.add(item);
    }

    public ImmutableList<Item> getItems() {
        return ImmutableList.<Item>builder().addAll(items).build();
    }

    public void add(final Item item) {
        items.add(item);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void moveToExistingContainer(final Container existingContainer, final Item itemToMove) {
        existingContainer.add(itemToMove);
        removeItem(itemToMove);
    }

    public static Container create(final String containerName, final Item item) {
        return new Container(containerName, item);
    }

    public void removeItem(final Item item) {
        items.remove(item);
    }

    private String name;
    private List<Item> items = Lists.newArrayList();
    private String description;
}
