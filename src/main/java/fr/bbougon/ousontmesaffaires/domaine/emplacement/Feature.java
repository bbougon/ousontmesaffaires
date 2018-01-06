package fr.bbougon.ousontmesaffaires.domaine.emplacement;

import com.google.common.base.Objects;

public class Feature {

    @SuppressWarnings("UnusedDeclaration")
    public Feature() {
    }

    public Feature(final Type type, final String feature) {
        this.type = type;
        this.feature = feature;
    }

    public Type getType() {
        return type;
    }

    public String getFeature() {
        return feature;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature feature1 = (Feature) o;
        return Objects.equal(type, feature1.type) &&
                Objects.equal(feature, feature1.feature);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, feature);
    }

    private Type type;
    private String feature;
}
