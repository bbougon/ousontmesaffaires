package fr.bbougon.ousontmesaffaires.command.container;

import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.CommandHandler;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;

public class ContainerPatchCommandHandler implements CommandHandler<ContainerPatchCommand, String> {

    @Override
    public Pair<String, Object> execute(final ContainerPatchCommand containerPatchCommand) {
        Container container = Repositories.containerRepository().findById(containerPatchCommand.getUUID());
        containerPatchCommand.getPatch().getFields().forEach(field -> {
            try {
                Field declaredField = container.getClass().getDeclaredField(field.getFieldName());
                declaredField.setAccessible(true);
                declaredField.set(container, field.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        String result = new GsonBuilder().create().toJson(
                JsonMappers.fromContainer(containerPatchCommand.getContainerId()).map(container));
        return Pair.of(result, container);
    }
}
