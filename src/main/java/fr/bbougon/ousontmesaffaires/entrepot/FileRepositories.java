package fr.bbougon.ousontmesaffaires.entrepot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;

import java.util.Map;
import java.util.ResourceBundle;

import static fr.bbougon.ousontmesaffaires.Configuration.DataBaseConfiguration;

public abstract class FileRepositories {

    public static void initialise(FileRepositories repositories) {
        SingletonHolder.INSTANCE = repositories;
    }

    public static FileRepository<ServerConfiguration> serverConfiguration() {
        return SingletonHolder.INSTANCE.getServerConfiguration();
    }

    public abstract FileRepository<ServerConfiguration> getServerConfiguration();

    public static FileRepository<DataBaseConfiguration> dataBaseConfiguration() {
        return SingletonHolder.INSTANCE.getDataBaseConfiguration();
    }

    protected abstract FileRepository<DataBaseConfiguration> getDataBaseConfiguration();

    private static class SingletonHolder {
        static FileRepositories INSTANCE = new FileRepositories() {
            @Override
            public FileRepository<ServerConfiguration> getServerConfiguration() {
                return() -> new ServerConfiguration() {
                    @Override
                    public String getDescriptor() {
                        return bundleMapped().get("server.descriptor");
                    }

                    @Override
                    public int getPort() {
                        return Integer.parseInt(bundleMapped().get("server.port"));
                    }
                };
            }

            @Override
            protected FileRepository<DataBaseConfiguration> getDataBaseConfiguration() {
                return () -> (DataBaseConfiguration) () -> {
                    Map<String, String> configuration = bundleMapped();
                    String host = configuration.get("database.host");
                    int port = Integer.parseInt(configuration.get("database.port"));
                    String dataBase = configuration.get("database.name");
                    return Settings.defaultInstance()
                            .withDatabase(new MongoClient(new ServerAddress(host, port), Lists.newArrayList()).getDatabase(dataBase))
                            .withDefaultUpdateStrategy(UpdateStrategies.DIFF);
                };
            }

            private Map<String, String> bundleMapped() {
                ResourceBundle bundle = ResourceBundle.getBundle("configuration");
                Map<String, String> result = Maps.newHashMap();
                bundle.keySet().forEach(key -> result.put(key, bundle.getString(key)));
                return result;
            }
        };
    }
}
