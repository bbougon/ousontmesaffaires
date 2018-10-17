package fr.bbougon.ousontmesaffaires.web.ressources;

import com.google.common.collect.Lists;
import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.container.image.Image;
import fr.bbougon.ousontmesaffaires.domain.container.image.ResizedImage;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGeneratorForTest;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.repositories.Repositories;
import fr.bbougon.ousontmesaffaires.repositories.WithMemoryRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class ContainerResourceTest {

    @Rule
    public WithMemoryRepositories withMemoryRepositories = new WithMemoryRepositories();

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Before
    public void before() {
        containerResource = new ContainerResource();
        withCommandBus.apply((CommandBus commandBus) -> containerResource.commandBus = commandBus);
        containerResource.qrGenerator = new QRGeneratorForTest();
    }

    @Test
    public void canGetAContainer() {
        Container container = Container.create("Bureau", Lists.newArrayList(Item.create("tshirt blanc taille 3 ans")));
        addImagesToContainer(container);
        Repositories.containerRepository().persist(container);
        String containerId = new Codec().urlSafeToBase64(container.getId().toString());

        Response response = containerResource.getContainer(containerId);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(new FileUtilsForTest("json/expectedJsonResult.json").getContent()
                .replace("ID_TO_REPLACE", containerId)
                .replace("FOLDER_NAME", container.getItems().get(0).getImageStore().getFolder())
                .replace("HASH_TO_REPLACE", SecurityService.sha1().cypher(new FileUtilsForTest("test/expectedSingleFormattedItem.txt")
                        .getContent()
                        .replace("FOLDER_NAME", container.getItems().get(0).getImageStore().getFolder()).getBytes())));
    }

    @Test
    public void canGetAllContainers() {
        Container container1 = Container.create("Container 1", Lists.newArrayList(Item.create("chaussure")));
        addImagesToContainer(container1);
        Container container2 = Container.create("Container 2", Lists.newArrayList(Item.create("pantalon")));
        addImagesToContainer(container2);
        Repositories.containerRepository().persist(container1);
        Repositories.containerRepository().persist(container2);

        Response response = containerResource.getAll(new UriInfoBuilderForTest().forContainers());

        assertThat(response.getEntity()).isEqualTo(new FileUtilsForTest("json/expectedJsonsResult.json").getContent()
                .replace("ID_TO_REPLACE_1", new Codec().urlSafeToBase64(container1.getId().toString()))
                .replace("FOLDER_NAME_1", container1.getItems().get(0).getImageStore().getFolder())
                .replace("HASH_TO_REPLACE_1", SecurityService.sha1().cypher(new FileUtilsForTest("test/expectedFormattedItem.txt")
                        .getContent()
                        .replace("FOLDER_NAME", container1.getItems().get(0).getImageStore().getFolder()).getBytes()))
                .replace("ID_TO_REPLACE_2", new Codec().urlSafeToBase64(container2.getId().toString()))
                .replace("FOLDER_NAME_2", container2.getItems().get(0).getImageStore().getFolder())
                .replace("HASH_TO_REPLACE_2", SecurityService.sha1().cypher(new FileUtilsForTest("test/expectedSecondFormattedItem.txt")
                        .getContent()
                        .replace("FOLDER_NAME", container2.getItems().get(0).getImageStore().getFolder()).getBytes())));
    }

    private void addImagesToContainer(final Container container) {
        container.getItems().get(0).add(Image.create("signature", "url", "secureUrl",
                Lists.newArrayList(
                        ResizedImage.create("url2", "secureUrl2", 100, 200),
                        ResizedImage.create("url3", "secureUrl3", 200, 400))
        ));
        container.getItems().get(0).add(Image.create("signature2", "url4", "secureUrl4",
                Lists.newArrayList(
                        ResizedImage.create("url5", "secureUrl5", 100, 200),
                        ResizedImage.create("url6", "secureUrl6", 200, 400))
        ));
    }

    private ContainerResource containerResource;
}
