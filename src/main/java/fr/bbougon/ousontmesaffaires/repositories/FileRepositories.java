package fr.bbougon.ousontmesaffaires.repositories;

import fr.bbougon.ousontmesaffaires.Configuration.ServerConfiguration;

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
        static FileRepositories INSTANCE = new DefaultFileRepositories();
    }

}
