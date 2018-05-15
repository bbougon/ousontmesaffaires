package fr.bbougon.ousontmesaffaires.command.mappers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.bbougon.ousontmesaffaires.command.container.ContainerField;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.infrastructure.security.SecurityService;
import fr.bbougon.ousontmesaffaires.infrastructure.security.WithSecurityService;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.junit.Rule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContainerJsonObjectMapperTest {

    @Rule
    public WithSecurityService withSecurityService = new WithSecurityService();

    @Test
    public void canMapWithHash() {
        Container container = new Gson().fromJson(new FileUtilsForTest("json/container.json").getContent(), Container.class);

        JsonObject containerJson = new ContainerJsonObjectMapper().map(container, new ContainerField("an-id"));

        String hash = containerJson.getAsJsonArray("items").get(0).getAsJsonObject().get("hash").getAsString();
        assertThat(hash).isEqualTo(SecurityService.sha1().encrypt(EXPECTED_ENCRYPTION.getBytes()));
    }

    private static final String EXPECTED_ENCRYPTION = "type;chaussure|folder_name|" +
            "signature;url;secureUrl;assets/testing/url2.png;assets/testing/secureUrl2.png;110.0;80.0;" +
            "assets/testing/url3.png;assets/testing/secureUrl3.png;552.0;400.0;assets/testing/url4.png;assets/testing/secureUrl4.png;1103.0;800.0|" +
            "signature2;url5;secureUrl5;assets/testing/url6.png;assets/testing/secureUrl6.png;110.0;80.0;" +
            "assets/testing/url7.png;assets/testing/secureUrl7.png;552.0;400.0;assets/testing/url8.png;assets/testing/secureUrl8.png;1103.0;800.0";
}