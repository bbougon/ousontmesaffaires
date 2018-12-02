package fr.bbougon.ousontmesaffaires.domain.container;

public class NLPAnalyzedItem {
    public NLPAnalyzedItem(final Item item) {

        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    private Item item;
}
