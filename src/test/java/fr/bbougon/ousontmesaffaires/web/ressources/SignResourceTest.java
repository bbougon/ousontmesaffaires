package fr.bbougon.ousontmesaffaires.web.ressources;

import fr.bbougon.ousontmesaffaires.command.WithCommandBus;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.rules.WithFileRepositories;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class SignResourceTest {

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Rule
    public WithCommandBus withCommandBus = new WithCommandBus();

    @Rule
    public WithFileRepositories withFileRepositories = new WithFileRepositories();

    @Test
    public void canSign() {
        SignResource signResource = new SignResource();
        withCommandBus.apply(commandBus -> signResource.commandBus = commandBus);

        Response response = signResource.sign(new FileUtilsForTest("json/dataToEncrypt.json").getContent());

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo("bfd09f95f331f558cbd1320e67aa8d488770583e");
    }
}