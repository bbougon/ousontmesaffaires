package fr.bbougon.ousontmesaffaires.entrepot.mongo;

import fr.bbougon.ousontmesaffaires.domaine.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepot;
import org.mongolink.MongoSession;

public class EntrepotEmplacementMongo extends EntrepotMongo<Emplacement> implements Entrepot<Emplacement> {

    public EntrepotEmplacementMongo(MongoSession session) {
        super(session);
    }

}
