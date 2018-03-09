package fr.bbougon.ousontmesaffaires.infrastructure.module.transactional;

import com.google.inject.Inject;
import fr.bbougon.ousontmesaffaires.OuSontMesAffairesApplicationForTest;
import fr.bbougon.ousontmesaffaires.command.location.LocationAddCommand;
import fr.bbougon.ousontmesaffaires.command.location.LocationsGetCommand;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.bus.CommandResponse;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.infrastructure.qrcode.QRGenerator;
import fr.bbougon.ousontmesaffaires.repositories.mongo.LocationMongoRepository;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtils;
import fr.bbougon.ousontmesaffaires.web.helpers.Codec;
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
    private Codec codec;

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
    public void canGetEmptyLocations() {
        CommandResponse commandResponse = commandBus.send(new LocationsGetCommand(codec, qrGenerator));

        assertThat(commandResponse.getResponse()).isEqualTo("[]");
    }

    @Test
    public void canAddALocation() {
        LocationAddCommand locationAddCommand = new LocationAddCommand(new FileUtils("json/t-shirt.json").getContent());

        commandBus.send(locationAddCommand);

        mongolinkSessionManager.start();
        List<Location> locations = new LocationMongoRepository(mongolinkSessionManager).getAll();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getItems()).contains(locationAddCommand.getLocation().getItems().get(0));
    }

}