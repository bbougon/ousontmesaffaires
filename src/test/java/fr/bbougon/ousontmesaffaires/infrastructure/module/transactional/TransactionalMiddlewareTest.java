package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import fr.bbougon.ousontmesaffaires.OuSontMesAffairesApplicationForTest;
import fr.bbougon.ousontmesaffaires.command.container.ContainerAddCommand;
import fr.bbougon.ousontmesaffaires.command.container.ContainersGetCommand;
import fr.bbougon.ousontmesaffaires.command.extracteditem.ExtractedItemAddItemCommand;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.domain.container.Feature;
import fr.bbougon.ousontmesaffaires.domain.container.Item;
import fr.bbougon.ousontmesaffaires.domain.extracteditem.ExtractedItem;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.infrastructure.security.Sha1Encryptor;
import fr.bbougon.ousontmesaffaires.repositories.mongo.ContainerMongoRepository;
import fr.bbougon.ousontmesaffaires.repositories.mongo.ExtractedItemMongoRepository;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
import fr.bbougon.ousontmesaffaires.web.helpers.ItemStringFormatter;
import fr.bbougon.ousontmesaffaires.web.ressources.UriInfoBuilderForTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongolink.MongoSession;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionalMiddlewareTest {

    @Inject
    private CommandBus commandBus;

    @Inject
    private QRGenerator qrGenerator;

    @Inject
    private MongolinkSessionManager mongolinkSessionManager;

    @Before
    public void before() {
        OuSontMesAffairesApplicationForTest ouSontMesAffairesApplication = new OuSontMesAffairesApplicationForTest();
        ouSontMesAffairesApplication.getInjector().injectMembers(this);
    }

    @After
    public void after() {
        MongoSession session = mongolinkSessionManager.getSession();
        session.getDb().drop();
    }

    @Test
    public void canGetEmptyContainers() {
        CommandResponse commandResponse = commandBus.send(new ContainersGetCommand(qrGenerator, new UriInfoBuilderForTest().forContainers()));

        assertThat(commandResponse.getResponse()).isEqualTo("[]");
    }

    @Test
    public void canAddContainer() {
        ContainerAddCommand containerAddCommand = new ContainerAddCommand(new FileUtilsForTest("json/t-shirt.json").getContent());

        commandBus.send(containerAddCommand);

        mongolinkSessionManager.start();
        List<Container> containers = new ContainerMongoRepository(mongolinkSessionManager).getAll();
        assertThat(containers).isNotEmpty();
        assertThat(containers.get(0).getItems()).contains(containerAddCommand.getContainer().getItems().get(0));
    }

    @Test
    public void canAddExtractedItem() {
        Item item = Item.create(Lists.newArrayList(Feature.create("type", "value")));
        Container container = Container.create("Name", item);
        mongolinkSessionManager.start();
        new ContainerMongoRepository(mongolinkSessionManager).persist(container);
        String itemHash = new Sha1Encryptor().encrypt(new ItemStringFormatter(item).format().getBytes());
        ExtractedItemAddItemCommand extractedItemAddItemCommand = new ExtractedItemAddItemCommand(new Codec().toBase64(container.getId().toString().getBytes()), itemHash);

        commandBus.send(extractedItemAddItemCommand);

        mongolinkSessionManager.start();
        List<ExtractedItem> extractedItems = new ExtractedItemMongoRepository(mongolinkSessionManager).getAll();
        assertThat(extractedItems).isNotEmpty();
        assertThat(extractedItems.get(0).getSourceContainer().getId()).isEqualTo(container.getId());
    }

}