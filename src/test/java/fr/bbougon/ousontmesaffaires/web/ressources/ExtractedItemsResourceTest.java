package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.UUID;

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ExtractedItemsResourceTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Before
    public void before() {
        resource = new ExtractedItemsResource();
        withCommandBus.apply((CommandBus commandBus) -> resource.commandBus = commandBus);
    }

    @Test
    public void canAddExtractedItem() {
        Item item = Item.create("pantalon");
        Container container = Container.create("Cave", Lists.newArrayList(item));
        Repositories.containerRepository().persist(container);
        String itemHash = new Sha1Encryptor().cypher(new ItemStringFormatter(item).format().getBytes());

        Response response = resource.addItem("{\"containerId\":\"" + new Codec().urlSafeToBase64(container.getId().toString()) + "\",\"itemHash\":\"" + itemHash + "\"}");

        assertThat(response.getStatus()).isEqualTo(CREATED.getStatusCode());
        assertThat(response.getLocation().getPath()).matches("^/extracted-items/[a-zA-Z0-9]{48}");
        assertThat(Repositories.containerRepository().getAll().get(0).getItems()).hasSize(0);
        assertThat(Repositories.extractedItemRepository().getAll()).hasSize(1);
        ExtractedItem extractedItem = Repositories.extractedItemRepository().getAll().get(0);
        assertThat(extractedItem.getItem()).isEqualTo(item);
        assertThat(extractedItem.getSourceContainer()).isEqualTo(container);
    }

    @Test
    public void returns404IfUnknownItem() {
        Item item = Item.create("pantalon");
        Container container = Container.create("Cave", Lists.newArrayList(item));
        Repositories.containerRepository().persist(container);

        Response response = resource.addItem("{\"containerId\":\"" + new Codec().urlSafeToBase64(container.getId().toString()) + "\",\"itemHash\":\"unknown hash\"}");

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void canGetAllExtractedItems() {
        ExtractedItem extractedItem = createAndPersist();
        ExtractedItem extractedItem2 = createAndPersist();

        Response response = resource.getAll();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(new Gson().toJson(response.getEntity())).isEqualTo("[{\"id\":\"" + new Codec().urlSafeToBase64(extractedItem.getId().toString()) + "\",\"item\":{\"item\":\"value\"," +
                "\"imageStore\":{\"folder\":\"" + extractedItem.getItem().getImageStore().getFolder() + "\",\"images\":[]},\"itemHash\":\"" + extractedItem.getItem().getItemHash() + "\",\"features\":[]}," +
                "\"sourceContainerId\":\"" + new Codec().urlSafeToBase64(extractedItem.getSourceContainer().getId().toString()) + "\",\"sourceContainerName\":\"name\"}" +
                ",{\"id\":\"" + new Codec().urlSafeToBase64(extractedItem2.getId().toString()) + "\",\"item\":{\"item\":\"value\"," +
                "\"imageStore\":{\"folder\":\"" + extractedItem2.getItem().getImageStore().getFolder() + "\",\"images\":[]},\"itemHash\":\"" + extractedItem2.getItem().getItemHash() + "\",\"features\":[]}," +
                "\"sourceContainerId\":\"" + new Codec().urlSafeToBase64(extractedItem2.getSourceContainer().getId().toString()) + "\",\"sourceContainerName\":\"name\"}]");
    }

    @Test
    public void canGetExtractedItem() {
        ExtractedItem extractedItem = createAndPersist();

        Response response = resource.get(new Codec().urlSafeToBase64(extractedItem.getId().toString()));

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(new Gson().toJson(response.getEntity())).isEqualTo("{\"id\":\"" + new Codec().urlSafeToBase64(extractedItem.getId().toString()) + "\"," +
                "\"item\":{\"item\":\"value\",\"imageStore\":{\"folder\":\"" + extractedItem.getItem().getImageStore().getFolder() + "\",\"images\":[]}," +
                "\"itemHash\":\"" + extractedItem.getItem().getItemHash() + "\",\"features\":[]}," +
                "\"sourceContainerId\":\"" + new Codec().urlSafeToBase64(extractedItem.getSourceContainer().getId().toString()) + "\",\"sourceContainerName\":\"name\"}");
    }

    @Test
    public void returns404OnNotFoundExtractedItem() {
        Response response = resource.get(new Codec().urlSafeToBase64(UUID.randomUUID().toString()));

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    private ExtractedItem createAndPersist() {
        ExtractedItem extractedItem = ExtractedItem.create(
                Item.create("value"),
                Container.create("name", Lists.newArrayList(Item.create("value1"))));
        Repositories.extractedItemRepository().persist(extractedItem);
        return extractedItem;
    }

    private ExtractedItemsResource resource;
}