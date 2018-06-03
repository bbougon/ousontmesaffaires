package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemField;
import fr.bbougon.ousontmesaffaires.command.mappers.JsonMappers;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

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
        String payload = "{\"name\":\"Cave\",\"item\":{\"type\":\"pantalon\"}}";
        Item item = Item.create(Features.getFeaturesFromPayload(payload));
        Container container = Container.create(ContainerName.getNameFromPayload(payload), item);
        Repositories.containerRepository().persist(container);
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(item).format().getBytes());

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
        String payload = "{\"name\":\"Cave\",\"item\":{\"type\":\"pantalon\"}}";
        Item item = Item.create(Features.getFeaturesFromPayload(payload));
        Container container = Container.create(ContainerName.getNameFromPayload(payload), item);
        Repositories.containerRepository().persist(container);

        Response response = resource.addItem("{\"containerId\":\"" + new Codec().urlSafeToBase64(container.getId().toString()) + "\",\"itemHash\":\"unknown hash\"}");

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void canGetAllExtractedItems() {
        ExtractedItem extractedItem = ExtractedItem.create(
                Item.create(Lists.newArrayList(Feature.create("type", "value"))),
                Container.create("name", Item.create(Lists.newArrayList(Feature.create("type1", "value1")))));
        Repositories.extractedItemRepository().persist(extractedItem);

        Response response = resource.getAll();

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new GsonBuilder()
                .create()
                .toJson(JsonMappers.fromExtractedItem().map(Lists.newArrayList(extractedItem),
                        (ExtractedItem extracted) -> new ExtractedItemField(new Codec().urlSafeToBase64(extracted.getId().toString())))));
    }

    private ExtractedItemsResource resource;
}