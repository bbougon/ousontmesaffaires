package fr.bbougon.ousontmesaffaires.command;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public enum PatchStrategy {
    DESCRIPTION(new DescriptionStrategy());

    PatchStrategy(final Strategy strategy) {
        this.strategy = strategy;
    }

    public static PatchStrategy of(final Patch patch) {
        Optional<PatchStrategy> optionalStrategy = Arrays.stream(values())
                .filter(patchStrategy -> patchStrategy.name().toLowerCase().equals(patch.getTarget()))
                .findFirst();
        optionalStrategy.ifPresent(strategy -> strategy.setPatch(patch));
        return optionalStrategy.orElseThrow(() -> new PatchException("An error occured during patch, current target 'unknown' is unknown."));
    }

    public void apply(final Supplier supplier) {
        this.strategy.apply(patch, supplier);
    }

    public void setPatch(final Patch patch) {
        this.patch = patch;
    }

    private final Strategy strategy;

    private Patch patch;
}
