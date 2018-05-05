package fr.bbougon.ousontmesaffaires.command;

import java.util.function.Supplier;

public interface Strategy {
    void apply(Patch patch, final Supplier supplier);
}
