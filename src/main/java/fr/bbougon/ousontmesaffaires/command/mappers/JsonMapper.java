package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.function.Function;

public interface JsonMapper<T, V> {

    JsonElement map(List<T> objects, Function<T, V> function);

    JsonElement map(T object, V function);
}
