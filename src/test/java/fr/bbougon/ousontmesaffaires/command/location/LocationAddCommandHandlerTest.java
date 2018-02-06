package fr.bbougon.ousontmesaffaires.command.location;

import com.google.inject.Inject;
import fr.bbougon.ousontmesaffaires.OuSontMesAffairesApplicationForTest;
import fr.bbougon.ousontmesaffaires.command.CommandHandlers;
import fr.bbougon.ousontmesaffaires.domain.location.Location;
import fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink.MongolinkSessionManager;
import fr.bbougon.ousontmesaffaires.repositories.mongo.LocationMongoRepository;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtils;
import org.junit.*;
import org.mongolink.MongoSession;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class LocationAddCommandHandlerTest {

    @Inject
    private CommandHandlers commandHandlers;

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
        session.stop();
    }

    @Test
    public void canAddALocation() {
        LocationAddCommand locationAddCommand = new LocationAddCommand(new FileUtils("json/t-shirt.json").getContent());

        commandHandlers.locationAdd().execute(locationAddCommand);

        mongolinkSessionManager.start();
        List<Location> locations = new LocationMongoRepository(mongolinkSessionManager).getAll();
        assertThat(locations).isNotEmpty();
        assertThat(locations.get(0).getItems()).contains(locationAddCommand.getLocation().getItems().get(0));
    }

}