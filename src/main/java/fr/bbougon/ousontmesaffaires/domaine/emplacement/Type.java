package fr.bbougon.ousontmesaffaires.domaine.emplacement;

import com.google.common.base.Objects;

public class Type {

    @SuppressWarnings("UnusedDeclaration")
    public Type() {
    }

    public Type(final String type) {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type1 = (Type) o;
        return Objects.equal(type, type1.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    public String getType() {
        return type;
    }

    private String type;
}
