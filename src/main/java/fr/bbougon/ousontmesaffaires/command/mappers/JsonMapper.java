package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonElement;

import java.util.List;

public interface JsonMapper<T> {

    JsonElement map(List<T> objects);

    JsonElement map(T object);
}