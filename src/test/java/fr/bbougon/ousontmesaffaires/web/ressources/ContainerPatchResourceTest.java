package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class ContainerPatchResourceTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Before
    public void before() {
        containerResource = initialise();
    }

    @Test
    public void canPatchAContainer() {
        Container container = Container.create("Container 1", Lists.newArrayList(Item.create("chaussure")));
        Repositories.containerRepository().persist(container);

        Response response = containerResource.patchContainer(new Codec().urlSafeToBase64(container.getId().toString()), "{\"target\":\"item.description\",\"id\":\"\",\"version\":1,\"data\":\"A description\"}");

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(new Gson().toJson(response.getEntity())).isEqualTo("{\"id\":\"" + new Codec().urlSafeToBase64(container.getId().toString()) + "\"," +
                "\"name\":\"Container 1\",\"description\":\"A description\"," +
                "\"items\":[{\"item\":\"chaussure\",\"imageStore\":{\"folder\":\"" + container.getItems().get(0).getImageStore().getFolder() + "\",\"images\":[]}," +
                "\"itemHash\":\"" + SecurityService.sha1().cypher(new ItemStringFormatter(container.getItems().get(0)).format().getBytes()) + "\",\"features\":[]}]}");
    }

    private ContainerPatchResource initialise() {
        ContainerPatchResource containerPatchResource = new ContainerPatchResource();
        withCommandBus.apply((CommandBus commandBus) -> containerPatchResource.commandBus = commandBus);
        return containerPatchResource;
    }

    private ContainerPatchResource containerResource;

}