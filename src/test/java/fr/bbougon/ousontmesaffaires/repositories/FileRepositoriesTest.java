package fr.bbougon.ousontmesaffaires.repositories;

import fr.bbougon.ousontmesaffaires.Configuration;
import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;
import org.junit.Test;
import org.mongolink.UpdateStrategies;

import static org.assertj.core.api.Assertions.assertThat;

public class FileRepositoriesTest {

    @Test
    public void canGetServerConfiguration() {
        ServerConfiguration serverConfiguration = FileRepositories.serverConfiguration().get();

        assertThat(serverConfiguration.getSettings().getPort()).isEqualTo(8182);
    }

    @Test
    public void canGetDatabaseConfiguration() {
        Configuration.DataBaseConfiguration dataBaseConfiguration = FileRepositories.dataBaseConfiguration().get();

        assertThat(dataBaseConfiguration.getSettings().getUpdateStrategy()).isEqualTo(UpdateStrategies.DIFF);
        assertThat(dataBaseConfiguration.getSettings().buildDatabase().getName()).isEqualTo("ousontmesaffaires-test");
    }
}