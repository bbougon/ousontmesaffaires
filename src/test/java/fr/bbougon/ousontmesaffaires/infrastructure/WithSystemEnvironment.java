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
    static final String SERVER_PORT = "6789";

    static {
        VARIABLES.put("DATABASE_HOST", DATABASE_HOST);
        VARIABLES.put("DATABASE_PORT", DATABASE_PORT);
        VARIABLES.put("DATABASE_NAME", DATABASE_NAME);
        VARIABLES.put("SERVER_PORT", SERVER_PORT);
    }
}
