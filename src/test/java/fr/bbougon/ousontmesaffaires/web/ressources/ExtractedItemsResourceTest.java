package fr.bbougon.ousontmesaffaires.web.ressources;

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
import fr.bbougon.ousontmesaffaires.web.ressources.json.ContainerName;
import fr.bbougon.ousontmesaffaires.web.ressources.json.Features;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
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

        Response response = resource.addItem(new Codec().urlSafeToBase64(container.getId().toString()), itemHash);

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

        Response response = resource.addItem(new Codec().urlSafeToBase64(container.getId().toString()), "unknown Hash");

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    private ExtractedItemsResource resource;
}