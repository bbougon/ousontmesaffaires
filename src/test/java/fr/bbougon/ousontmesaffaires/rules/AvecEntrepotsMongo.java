package fr.bbougon.ousontmesaffaires.rules;

import fr.bbougon.ousontmesaffaires.entrepot.Entrepots;
import fr.bbougon.ousontmesaffaires.entrepot.mongo.EntrepotsMongos;
import org.junit.rules.ExternalResource;
import org.mongolink.test.MongolinkRule;

public class AvecEntrepotsMongo extends ExternalResource {

    public void before() throws Throwable {
        mongolinkRule = MongolinkRule.withPackage("fr.bbougon.ousontmesaffaires.entrepot.mongo.mapping");
        mongolinkRule.before();
        Entrepots.initialise(new EntrepotsMongos(mongolinkRule.getCurrentSession()));
    }

    @Override
    protected void after() {
        mongolinkRule.after();
    }

    public void cleanSession() {
        mongolinkRule.cleanSession();
    }

    private MongolinkRule mongolinkRule;
}
