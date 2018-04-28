package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.container.Field;

import java.util.List;

public class Patch {

    public Patch() {
    }

    public Patch(final List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    private List<Field> fields;

}
