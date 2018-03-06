package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.common.collect.Maps;
import org.junit.*;

import java.util.HashMap;

import static fr.bbougon.ousontmesaffaires.infrastructure.WithSystemEnvironment.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class ConfigurationPropertiesTest {

    @Rule
    public WithSystemEnvironment withSystemEnvironment = new WithSystemEnvironment();

    @Before
    public void before() {
        keys.put("database.host", "$DATABASE_HOST");
        keys.put("database.port", "$DATABASE_PORT");
        keys.put("database.name", "$DATABASE_NAME");
        keys.put("server.port", "$SERVER_PORT");
    }

    @Test
    public void canSetPropertiesFromStandardConfiguration() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties();

        assertThat(configurationProperties.databaseHost()).isEqualTo("127.0.0.1");
        assertThat(configurationProperties.databaseName()).isEqualTo("ousontmesaffaires-test");
        assertThat(configurationProperties.databasePort()).isEqualTo(27017);
        assertThat(configurationProperties.serverPort()).isEqualTo(8182);
    }

    @Test
    public void canSetPropertiesFromEnvironmentVariables() {
        ConfigurationProperties configurationProperties = new ConfigurationProperties(keys);

        assertThat(configurationProperties.databaseHost()).isEqualTo(DATABASE_HOST);
        assertThat(configurationProperties.databaseName()).isEqualTo(DATABASE_NAME);
        assertThat(configurationProperties.databasePort()).isEqualTo(Integer.parseInt(DATABASE_PORT));
        assertThat(configurationProperties.serverPort()).isEqualTo(Integer.parseInt(SERVER_PORT));
    }

    private HashMap<String, String> keys = Maps.newHashMap();
}