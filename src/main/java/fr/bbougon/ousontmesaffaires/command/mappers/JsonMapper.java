package fr.bbougon.ousontmesaffaires.command.mappers;

import java.util.List;
import java.util.function.Function;

public interface JsonMapper<T, R, V> {

    R map(List<T> objects, Function<T, V> function);

    R map(T object, Function<T, V> function);
}
