package fr.bbougon.ousontmesaffaires.domain.location;

import com.google.common.base.Objects;

public class Feature {

    @SuppressWarnings("UnusedDeclaration")
    Feature() {
    }

    private Feature(final String type, final String feature) {
        this.type = type;
        this.feature = feature;
    }

    public String getType() {
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

    public static Feature create(final String type, final String value) {
        return new Feature(type, value);
    }

    private String type;
    private String feature;
}
