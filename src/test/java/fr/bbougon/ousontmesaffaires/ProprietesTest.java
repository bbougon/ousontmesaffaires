package fr.bbougon.ousontmesaffaires;

import org.junit.Test;
import org.mongolink.Settings;
import org.mongolink.UpdateStrategies;

import static org.fest.assertions.Assertions.assertThat;


public class ProprietesTest {

    @Test
    public void onPeutAjouterDesProprietes() {
        Settings settings = Settings.defaultInstance();
        Proprietes proprietes = new Proprietes();

        proprietes.ajouteLesProprietes(settings);

        assertThat(proprietes.getNomBaseDeDonnees()).isEqualTo("basededonnees");
        assertThat(proprietes.getServerAddress().getHost()).isEqualTo("127.0.0.1");
        assertThat(proprietes.getServerAddress().getPort()).isEqualTo(27017);
        assertThat(proprietes.getDefaultStrategy()).isEqualTo(UpdateStrategies.DIFF);
        assertThat(proprietes.getUser()).isEqualTo("user");
        assertThat(proprietes.getPassword()).isEqualTo("password");
    }

}