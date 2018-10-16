package fr.bbougon.ousontmesaffaires.web.helpers;

import com.google.gson.Gson;
import fr.bbougon.ousontmesaffaires.domain.container.Container;
import fr.bbougon.ousontmesaffaires.test.utils.FileUtilsForTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemStringFormatterTest {

    @Test
    public void canFormat() {
        Container container = new Gson().fromJson(new FileUtilsForTest("json/container.json").getContent(), Container.class);

        assertThat(new ItemStringFormatter(container.getItems().get(0)).format()).isEqualTo(EXPECTED_FIRST_ITEM_FORMAT);
        assertThat(new ItemStringFormatter(container.getItems().get(1)).format()).isEqualTo(EXPECTED_SECOND_ITEM_FORMAT);
    }

    private static final String EXPECTED_FIRST_ITEM_FORMAT = "chaussure|folder_name|" +
            "signature;url;secureUrl;assets/testing/url2.png;assets/testing/secureUrl2.png;110.0;80.0;" +
            "assets/testing/url3.png;assets/testing/secureUrl3.png;552.0;400.0;assets/testing/url4.png;assets/testing/secureUrl4.png;1103.0;800.0|" +
            "signature2;url5;secureUrl5;assets/testing/url6.png;assets/testing/secureUrl6.png;110.0;80.0;" +
            "assets/testing/url7.png;assets/testing/secureUrl7.png;552.0;400.0;assets/testing/url8.png;assets/testing/secureUrl8.png;1103.0;800.0";
    private static final String EXPECTED_SECOND_ITEM_FORMAT = "pantalon marron|folder_name_2|" +
            "signature_2;assets/testing/url5.png;assets/testing/secureUrl5.png;assets/testing/url6.png;assets/testing/secureUrl6.png;110.0;80.0;" +
            "assets/testing/url7.png;assets/testing/secureUrl7.png;552.0;400.0;assets/testing/url8.png;assets/testing/secureUrl8.png;1103.0;800.0";
}