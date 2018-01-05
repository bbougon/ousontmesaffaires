package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.boundaries.LocationResource;
import org.junit.Test;

import java.util.Set;

import static org.fest.assertions.api.Assertions.assertThat;

public class MyApplicationTest {

    @Test
    public void allResourcesAreLoaded() throws Exception {
        Set<Class<?>> classes = new MyApplication().getClasses();

        assertThat(classes).contains(LocationResource.class);
    }
}