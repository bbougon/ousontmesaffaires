package fr.bbougon.ousontmesaffaires.web.ressources.json;

class FeatureJSON {

    FeatureJSON(final String type, final String value) {
        this.type = type;
        this.value = value;
    }

    String getType() {
        return type;
    }

    String getValue() {
        return value;
    }

    private String type;
    private String value;
}
