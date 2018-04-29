package fr.bbougon.ousontmesaffaires.command;

import fr.bbougon.ousontmesaffaires.command.container.Field;

import java.util.List;
import java.util.function.Supplier;

public class Patch {

    public Patch(final List<Field> fields) {
        this.fields = fields;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void apply(final Supplier supplier) {
        getFields().forEach(field -> {
            try {
                Object object = supplier.get();
                java.lang.reflect.Field declaredField = object.getClass().getDeclaredField(field.getFieldName());
                declaredField.setAccessible(true);
                declaredField.set(object, field.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Field> fields;

}
