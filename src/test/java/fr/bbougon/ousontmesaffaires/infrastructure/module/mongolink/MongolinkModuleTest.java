package fr.bbougon.ousontmesaffaires.infrastructure.module.mongolink;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mongolink.Settings;

import static org.assertj.core.api.Assertions.assertThat;

public class MongolinkModuleTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Inject
    private MongolinkSessionManager mongolinkSessionManager;

    @Test
    public void canCreateModule() {
        MongolinkModule mongolinkModule = new MongolinkModule("fr.bbougon", Settings.defaultInstance());
        Injector injector = Guice.createInjector(mongolinkModule, BoundFieldModule.of(this));
        injector.injectMembers(this);

        assertThat(mongolinkSessionManager).isNotNull();
    }

    @Test
    public void checkThatMappingPackageIsNotNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("mapping package cannot be null or empty.");
        new MongolinkModule(null, Settings.defaultInstance());
    }

    @Test
    public void checkThatMappingPackageIsNotEmpty() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("mapping package cannot be null or empty.");
        new MongolinkModule("", Settings.defaultInstance());
    }

    @Test
    public void checkThatMongoSettingsIsNotNull() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Mongo settings cannot be null.");
        new MongolinkModule("fr.bbougon", null);
    }
}