package fr.bbougon.ousontmesaffaires.command.mappers;

public interface JsonMapper<T, R> {

    R map(T object);

}
