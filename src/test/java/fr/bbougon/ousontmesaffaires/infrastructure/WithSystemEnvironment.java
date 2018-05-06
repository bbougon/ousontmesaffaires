package fr.bbougon.ousontmesaffaires.infrastructure;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.rules.ExternalResource;

import java.util.Map;

public class WithSystemEnvironment extends ExternalResource {

    @Override
    @Before
    public void before() throws Throwable {
        super.before();
        SystemEnvironment.SingletonHolder.INSTANCE = new SystemEnvironment() {
            @Override
            String env(final String key) {
                return VARIABLES.get(key);
            }
        };
    }


    private static Map<String, String> VARIABLES = Maps.newHashMap();

    static final String DATABASE_HOST = "a-host";
    static final String DATABASE_PORT = "12345";
    static final String DATABASE_NAME = "database";
    static final String DATABASE_USER = "a-user";
    static final String DATABASE_PASSWORD = "a-password";
    static final String SERVER_PORT = "6789";
    private static final String SERVER_SECURED = "true";
    private static final String THIRDPARTSERVICE_IMAGE_SECRET = "abcd";
    private static final String THIRDPARTSERVICE_IMAGE_APIKEY = "12345";

    static {
        VARIABLES.put("DATABASE_HOST", DATABASE_HOST);
        VARIABLES.put("DATABASE_PORT", DATABASE_PORT);
        VARIABLES.put("DATABASE_NAME", DATABASE_NAME);
        VARIABLES.put("DATABASE_USER", DATABASE_USER);
        VARIABLES.put("DATABASE_PASSWORD", DATABASE_PASSWORD);
        VARIABLES.put("SERVER_PORT", SERVER_PORT);
        VARIABLES.put("SERVER_SECURED", SERVER_SECURED);
        VARIABLES.put("THIRDPARTSERVICE_IMAGE_SECRET", THIRDPARTSERVICE_IMAGE_SECRET);
        VARIABLES.put("THIRDPARTSERVICE_IMAGE_APIKEY", THIRDPARTSERVICE_IMAGE_APIKEY);
    }
}
