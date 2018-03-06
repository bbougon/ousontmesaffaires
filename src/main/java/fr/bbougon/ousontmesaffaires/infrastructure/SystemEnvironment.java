package fr.bbougon.ousontmesaffaires.infrastructure;

public class SystemEnvironment {

    public static String getEnv(final String key) {
        return SingletonHolder.INSTANCE.env(key);
    }

    String env(final String key) {
        return System.getenv(key);
    }

    public static class SingletonHolder {
        public static SystemEnvironment INSTANCE = new SystemEnvironment();
    }
}
