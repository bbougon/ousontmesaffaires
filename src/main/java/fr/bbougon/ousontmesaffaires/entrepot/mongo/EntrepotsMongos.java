package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;
import org.mongolink.MongoSession;

public class EntrepotsMongos extends Entrepots {

    public EntrepotsMongos(MongoSession session) {
        this.session = session;
    }

    private final MongoSession session;
}
