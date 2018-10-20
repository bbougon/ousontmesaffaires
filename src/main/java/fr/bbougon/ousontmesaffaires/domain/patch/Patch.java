package fr.bbougon.ousontmesaffaires.domain.patch;

import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.PatchedContainer;

import java.util.function.Supplier;

public interface Patch<T> {

    PatchedContainer<T> apply(Supplier<Container> supplier);
}
