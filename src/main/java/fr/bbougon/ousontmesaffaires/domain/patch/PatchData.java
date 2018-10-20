package fr.bbougon.ousontmesaffaires.domain.patch;

public abstract class PatchData<T> {

    PatchData(final String id, final Object data) {
        this.id = id;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public abstract T getData();

    private String id;
    Object data;
}
