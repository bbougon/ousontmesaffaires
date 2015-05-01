package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepot;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;
import org.mongolink.MongoSession;

public class EntrepotsMongos extends Entrepots {

    public EntrepotsMongos(MongoSession session) {
        this.session = session;
    }

    @Override
    protected Entrepot<Emplacement> getEmplacement() {
        return new EntrepotEmplacementMongo(session);
    }

    private final MongoSession session;
}
