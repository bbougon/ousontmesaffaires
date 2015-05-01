package fr.bbougon.ousontmesaffaires.entrepot.memoire;


import fr.bbougon.ousontmesaffaires.domaine.Emplacement;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepot;
import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;

public class EntrepotsMemoire extends Entrepots {

    @Override
    protected Entrepot<Emplacement> getEmplacement() {
        return entrepotEmplacement;
    }

    private final EntrepotEmplacement entrepotEmplacement = new EntrepotEmplacement();
}
