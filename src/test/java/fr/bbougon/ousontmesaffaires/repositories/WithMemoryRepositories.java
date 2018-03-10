package fr.bbougon.ousontmesaffaires.repositories;

import org.junit.Before;
import org.junit.rules.ExternalResource;

public class WithMemoryRepositories extends ExternalResource {

    @Override
    @Before
    public void before() throws Throwable {
        Repositories.initialise(new MemoryRepositories());
        super.before();
    }
}
