package fr.bbougon.ousontmesaffaires.command.sticker;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Sticker {

    public Sticker(final String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    private byte[] content;
    private String name;


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Sticker sticker = (Sticker) o;

        return new EqualsBuilder()
                .append(name, sticker.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
