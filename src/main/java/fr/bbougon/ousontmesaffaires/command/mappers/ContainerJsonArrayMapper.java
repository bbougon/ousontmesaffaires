package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.JsonArray;
import fr.bbougon.ousontmesaffaires.command.container.ContainerField;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.function.Function;

public class ContainerJsonArrayMapper implements JsonMapper<Container, JsonArray, ContainerField> {

    ContainerJsonArrayMapper() {
    }

    @Override
    public JsonArray map(final List<Container> containers, final Function<Container, ContainerField> containerField) {
        JsonArray jsonArray = new JsonArray();
        containers
                .stream()
                .map(container -> new ContainerJsonObjectMapper().map(container, containerField.apply(container)))
                .forEach(jsonArray::add);
        return jsonArray;
    }

    @Override
    public JsonArray map(final Container object, final ContainerField containerField) {
        throw new NotImplementedException();
    }

}
