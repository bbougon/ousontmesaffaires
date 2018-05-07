package fr.bbougon.ousontmesaffaires.command;

import java.util.function.Supplier;

public class Patch {

    public void apply(final Supplier supplier) {
        PatchStrategy.of(this).apply(supplier);
    }

    public String getTarget() {
        return target;
    }

    public Object getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    private String target;
    private String id;
    private int version;
    private Object data;


}
