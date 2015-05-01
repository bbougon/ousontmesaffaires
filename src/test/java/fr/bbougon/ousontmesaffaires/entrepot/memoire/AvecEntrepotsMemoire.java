package fr.bbougon.ousontmesaffaires.entrepot.memoire;

import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;
import org.junit.rules.ExternalResource;

public class AvecEntrepotsMemoire extends ExternalResource {

    public AvecEntrepotsMemoire() {
        Entrepots.initialise(new EntrepotsMemoire());
    }

}
