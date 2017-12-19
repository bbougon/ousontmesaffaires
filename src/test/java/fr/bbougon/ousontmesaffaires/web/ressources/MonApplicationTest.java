package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.boundaries.EmplacementRessource;
import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

public class MonApplicationTest {

    @Test
    public void toutesLesRessourcesSontChargees() throws Exception {
        Set<Class<?>> classes = new MonApplication().getClasses();

        assertThat(classes).contains(EmplacementRessource.class);
    }
}