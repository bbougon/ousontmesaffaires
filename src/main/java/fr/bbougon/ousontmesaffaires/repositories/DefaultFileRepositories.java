package fr.bbougon.ousontmesaffaires.repositories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import fr.bbougon.ousontmesaffaires.Configuration;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;

import java.util.Map;
import java.util.ResourceBundle;

public class DefaultFileRepositories extends FileRepositories {

    @Override
    public FileRepository<Configuration.ServerConfiguration> getServerConfiguration() {
        return() -> (Configuration.ServerConfiguration) () -> Integer.parseInt(bundleMapped().get("server.port"));
    }

    @Override
    protected FileRepository<Configuration.DataBaseConfiguration> getDataBaseConfiguration() {
        return () -> (Configuration.DataBaseConfiguration) () -> {
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
}
