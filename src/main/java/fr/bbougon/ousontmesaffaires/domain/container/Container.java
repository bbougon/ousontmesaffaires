package fr.bbougon.ousontmesaffaires.domain.container;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.domain.AggregateRoot;
import fr.bbougon.ousontmesaffaires.domain.BusinessError;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.patch.Description;
import fr.bbougon.ousontmesaffaires.infrastructure.nlp.NLPAnalysis;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Container extends AggregateRoot {

    @SuppressWarnings("UnusedDeclaration")
    Container() {
        super(UUID.randomUUID());
    }

    private Container(final String containerName, final List<Item> items) {
        this();
        this.name = containerName;
        this.items.addAll(items);
    }

    public ImmutableList<Item> getItems() {
        return ImmutableList.<Item>builder().addAll(items).build();
    }

    public void addItem(final Item item) {
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

    public static Container create(final String containerName, final List<Item> items) {
        return new Container(containerName, items);
    }

    public void removeItem(final Item item) {
        items.remove(item);
    }

    public ContainerItemAdded addItem(final String item) {
        Item itemAdded = Item.create(item);
        items.add(itemAdded);
        return new ContainerItemAdded(itemAdded.getItemHash());
    }

    public PatchedContainer<Item> patchImage(final Image image, final String itemHash) {
        Item patchedItem = items.stream()
                .filter(item -> item.getItemHash().equals(itemHash))
                .findFirst()
                .orElseThrow(() -> new BusinessError("UNKNOWN_ITEM_TO_PATCH", name));
        patchedItem.add(image);
        return new PatchedContainer<Item>() {

            @Override
            public Item getPatchedData() {
                return patchedItem;
            }
        };
    }

    public PatchedContainer<Description> updateDescription(final String description) {
        setDescription(description);
        return new PatchedContainer<Description>() {
            @Override
            public Description getPatchedData() {
                return new Description(Container.this);
            }
        };
    }

    public MovedItem moveItemTo(final String itemHash, final Container existingContainer) {
        Item itemToMove = getItems()
                .stream()
                .filter(item -> item.getItemHash().equals(itemHash))
                .findFirst()
                .orElseThrow(() -> new BusinessError("UNKNOWN_ITEM_TO_MOVE", existingContainer.name));
        return moveToExistingContainer(existingContainer, itemToMove);
    }

    private MovedItem moveToExistingContainer(final Container existingContainer, final Item itemToMove) {
        existingContainer.addItem(itemToMove);
        removeItem(itemToMove);
        return new MovedItem(itemToMove.getItemHash());
    }

    public List<NLPAnalyzedItem> processItemsNaturalAnalysis(final List<NLPAnalysis> nlpAnalyses) {
        return nlpAnalyses.stream()
                .map(NLPAnalyzedItem::new)
                .collect(Collectors.toList());
    }

    private String name;
    private List<Item> items = Lists.newArrayList();
    private String description;
}
