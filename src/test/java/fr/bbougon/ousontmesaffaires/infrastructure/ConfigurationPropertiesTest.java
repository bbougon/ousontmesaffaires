package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;

import static fr.bbougon.ousontmesaffaires.infrastructure.WithSystemEnvironment.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class ConfigurationPropertiesTest {

    @Rule
    public WithSystemEnvironment withSystemEnvironment = new WithSystemEnvironment();

    @Before
    public void before() {
        keys.put("database.host", "$DATABASE_HOST");
        keys.put("database.port", "$DATABASE_PORT");
        keys.put("database.name", "$DATABASE_NAME");
        keys.put("database.user", "$DATABASE_USER");
        keys.put("database.password", "$DATABASE_PASSWORD");
        keys.put("server.port", "$SERVER_PORT");
    }

    @Test
    public void canSetPropertiesFromStandardConfiguration() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();

        assertThat(configurationProperties.databaseHost()).isEqualTo("127.0.0.1");
        assertThat(configurationProperties.databaseName()).isEqualTo("ousontmesaffaires-test");
        assertThat(configurationProperties.databasePort()).isEqualTo(27017);
        assertThat(configurationProperties.serverPort()).isEqualTo(8182);
        assertThat(configurationProperties.databaseUser()).isEqualTo("user");
        assertThat(configurationProperties.databasePassword()).isEqualTo("password");
    }

    @Test
    public void canSetPropertiesFromEnvironmentVariables() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties(keys);

        assertThat(configurationProperties.databaseHost()).isEqualTo(DATABASE_HOST);
        assertThat(configurationProperties.databaseName()).isEqualTo(DATABASE_NAME);
        assertThat(configurationProperties.databasePort()).isEqualTo(Integer.parseInt(DATABASE_PORT));
        assertThat(configurationProperties.databaseUser()).isEqualTo(DATABASE_USER);
        assertThat(configurationProperties.databasePassword()).isEqualTo(DATABASE_PASSWORD);
        assertThat(configurationProperties.serverPort()).isEqualTo(Integer.parseInt(SERVER_PORT));
    }

    @Test
    public void handleNonExistingKey() {
        try {
            ConfigurationProperties configurationProperties = new ConfigurationProperties(Maps.newHashMap());
            configurationProperties.databaseHost();
            failBecauseExceptionWasNotThrown(ApplicationConfigurationException.class);
        } catch (ApplicationConfigurationException e) {
            assertThat(e.getMessage()).isEqualTo("Error during application configuration, key 'database.host' not provided!");
        }
    }

    private HashMap<String, String> keys = Maps.newHashMap();
}