package fr.bbougon.ousontmesaffaires.command.container;

public class Field {

    public Field(final String fieldName, final String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValue() {
        return value;
    }

    private String fieldName;
    private final String value;
}
