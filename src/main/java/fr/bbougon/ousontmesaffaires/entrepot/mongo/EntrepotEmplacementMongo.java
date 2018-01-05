package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.emplacement.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.*;
import org.mongolink.MongoSession;

public class EntrepotEmplacementMongo extends EntrepotMongo<Emplacement> implements EntrepotEmplacement {

    @SuppressWarnings("UnusedDeclaration")
    public EntrepotEmplacementMongo() {
        super(MongoConfiguration.createSession(EntrepotsFichier.configurationBaseDeDonnees().get().getSettings()));
    }

    EntrepotEmplacementMongo(final MongoSession currentSession) {
        super(currentSession);
    }
}
