package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigurationProperties {


    public ConfigurationProperties() {
        this(ResourceBundle.getBundle("configuration"));
    }

    ConfigurationProperties(final HashMap<String, String> keys) {
        this.keys = keys;
    }

    ConfigurationProperties(final ResourceBundle bundle) {
        this.keys = Maps.newHashMap();
        bundle.keySet().forEach(key -> keys.put(key, bundle.getString(key)));
    }

    public int serverPort() {
        LOGGER.debug("Configuring server port {}", getKeyValue(SERVER_PORT));
        return Integer.parseInt(getKeyValue(SERVER_PORT));
    }

    public int databasePort() {
        LOGGER.debug("Configuring database port: {}", getKeyValue(DATABASE_PORT));
        return Integer.parseInt(getKeyValue(DATABASE_PORT));
    }

    public String databaseHost() {
        LOGGER.debug("Configuring database host {}", getKeyValue(DATABASE_HOST));
        return getKeyValue(DATABASE_HOST);
    }

    public String databaseName() {
        LOGGER.debug("Configuring database name {}", getKeyValue(DATABASE_NAME));
        return getKeyValue(DATABASE_NAME);
    }

    public String databaseUser() {
        return getKeyValue(DATABASE_USER);
    }

    public String databasePassword() {
        return getKeyValue(DATABASE_PASSWORD);
    }

    public boolean secureServer() {
        String serverSecured = keys.get(SERVER_SECURED);
        if (serverSecured == null) {
            return false;
        }
        if (isEnvironmentVariable(SERVER_SECURED)) {
            serverSecured = SystemEnvironment.getEnv(serverSecured.substring(1));
        }
        return Boolean.parseBoolean(serverSecured);
    }

    public String thirdPartServiceSecret() {
        return getKeyValue(THIRDPARTSERVICE_IMAGE_SECRET);
    }

    public boolean hasCredentials() {
        return keys.get(DATABASE_USER) != null && keys.get(DATABASE_PASSWORD) != null;
    }

    private String getKeyValue(final String key) {
        checkKeyExistence(key);
        if (isEnvironmentVariable(key)) {
            return SystemEnvironment.getEnv(keys.get(key).substring(1));
        }
        return keys.get(key);
    }

    private void checkKeyExistence(final String key) {
        if (keys.get(key) == null) {
            throw  new ApplicationConfigurationException(String.format("Error during application configuration, key '%s' not provided!", key));
        }
    }

    private boolean isEnvironmentVariable(final String key) {
        return keys.get(key).startsWith(LINUX_ENV_PREFIX);
    }


    private final Map<String, String> keys;
    private static final String LINUX_ENV_PREFIX = "$";
    private static final String SERVER_PORT = "server.port";
    private static final String SERVER_SECURED = "server.secured";
    private static final String DATABASE_PORT = "database.port";
    private static final String DATABASE_HOST = "database.host";
    private static final String DATABASE_NAME = "database.name";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationProperties.class);
    private static final String DATABASE_USER = "database.user";
    private static final String DATABASE_PASSWORD = "database.password";
    private static final String THIRDPARTSERVICE_IMAGE_SECRET = "thirdpartservice.image.secret";
}
